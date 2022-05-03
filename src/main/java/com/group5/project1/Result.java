package com.group5.project1;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.*;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
//import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.appengine.api.datastore.*;
import com.google.cloud.datastore.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

/**
 * Servlet implementation class Result
 */
@WebServlet("/Result")
public class Result extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Result() {
		super();
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter op = response.getWriter();

		try {
			System.out.println("In Result.java");

			HttpSession session = request.getSession(true);

			String url = (String) request.getParameter("url");
			String id = (String) request.getParameter("id");

			String[] urlArray = url.split(",");
			String[] idArray = id.split(",");
			 /*
			 System.out.println("URL:"+url); System.out.println("ID:"+id);
			 op.println("<ul>"); 
			 for(String str : urlArray) { 
				 op.println("<li>"+str+"</li>"); 
				 }
			 op.println("</ul>"); 
			 op.println("<ul>"); 
			 for(String str : idArray) {
				 op.println("<li>"+str+"</li>"); 
			 	} 
			 op.println("</ul>");*/
			 ArrayList<String> listlable = new ArrayList<>();
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			//op.println(" <img src="+urlArray[0]+" alt=\"image\" width=\"500\" height=\"600\"> ");
			for(int i=0; i<idArray.length;i++){
				
				String FbPhotoId = idArray[i];
				url = (String) urlArray[i];
				if (checkIfImageExists(datastore, FbPhotoId) == false) {
		            List<EntityAnnotation> imageLabels = getImageLabels(url);
		            if (imageLabels != null) {
		                List<String> lables = imageLabels.stream().filter(label -> label.getScore() * 100 > 75)
		                        .map(EntityAnnotation::getDescription).collect(Collectors.toList());
		                String lable = lables.get(0);
		                if (null != lable && !lable.isEmpty()) {
		                	
		                	if(listlable.contains(lable)) {
		                		 op.println(lable);
		                	}else {
		                		 op.println("Present"+lable);
		                		listlable.add(lable);
		                	}

		                    addImageDetailsToDataStore(url, lable, FbPhotoId, datastore);
		                    //getImageFromStore(request, response, datastore, FbPhotoId);
		                }
						
		            }
		        }else{
		            //getImageFromStore(request, response, datastore, FbPhotoId);
		        	ArrayList<String> listlable2 = getLabelFromDataStore(request, response, datastore);
		        	int len = listlable2.size();
		        	op.println(len+"\n");
		        	for(int a = 0; a<len;a++) {
		        		if(listlable2.get(a) == null) {
		        			if(listlable.contains(listlable2.get(a))) {
			               		 op.println(listlable.get(a)+"\n");
			               	}else {
			               		 op.println("Present in else"+listlable2.get(a)+"\n");
			               		listlable.add(listlable2.get(a));
			               	}
		        		}
		        	}
		        	
		        }
			}
			for(String str : listlable){ op.println(str); }
			String[] arrList = new String[listlable.size()];
			for (int i = 0; i <listlable.size();i++ ) {
				arrList[i] = listlable.get(i);
			}
			//request.setAttribute("labelList", arrList);
			RequestDispatcher dispatcher = getServletContext()
                    .getRequestDispatcher("/labels.jsp");
			
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				op.print("Error:"+e);
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				op.print("Error:"+e);
				e.printStackTrace();
			}
            
			 
		} catch (Exception e) {
			op.println("error:" + e);
		}

		// doGet(request, response);
	}
	
	
	private ArrayList<String> getLabelFromDataStore(HttpServletRequest request, HttpServletResponse response,
			DatastoreService datastore) {
		// TODO Auto-generated method stub
		String Lable = null;
		 ArrayList<String> listlable = new ArrayList<>();
		
		 Query q = new Query("User_Images");
		 PreparedQuery pq = datastore.prepare(q);
		 List<Entity> results = pq.asList(FetchOptions.Builder.withDefaults());
	        if(null != results) {
	            results.forEach(user_Photo -> {
	            	String labelsFromStore = (String) user_Photo.getProperty("labels");
	            	listlable.add(labelsFromStore);
	            });
	        }
		return listlable;
	}

	public static void addImageDetailsToDataStore(String url, String labels, String imageId, DatastoreService
            datastore) {
        Entity User_Images = new Entity("User_Images");
        User_Images.setProperty("image_id", imageId);
        User_Images.setProperty("image_url", url);
        User_Images.setProperty("labels", labels);
        datastore.put(User_Images);
    }

    private void getImageFromStore(HttpServletRequest request, HttpServletResponse response, DatastoreService datastore, String imageId) {

        Query query =
                new Query("User_Images")
                        .setFilter(new Query.FilterPredicate("image_id", Query.FilterOperator.EQUAL, imageId));
        PreparedQuery pq = datastore.prepare(query);
        List<Entity> results = pq.asList(FetchOptions.Builder.withDefaults());
        if(null != results) {
            results.forEach(user_Photo -> {
                String labelsFromStore = (String) user_Photo.getProperty("labels");
                String image_url=user_Photo.getProperty("image_url").toString();
                request.setAttribute("imageUrl",image_url );
                request.setAttribute("imageLabels", labelsFromStore);
                RequestDispatcher dispatcher = getServletContext()
                        .getRequestDispatcher("/labels.jsp");
                
               try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                
            });
        }

    }

    public static boolean checkIfImageExists(DatastoreService datastore, String imageId) {
        Query q =
                new Query("User_Images")
                        .setFilter(new Query.FilterPredicate("image_id", Query.FilterOperator.EQUAL, imageId));
        PreparedQuery pq = datastore.prepare(q);
        Entity result = pq.asSingleEntity();
        if (result == null) {
            return false;
        }
        return true;
    }

	private static byte[] downloadFile(URL url) throws Exception {
        try{
        	
        	InputStream in = url.openStream();
        	
        	try (ByteArrayOutputStream bytearr = new ByteArrayOutputStream()) {
				byte[] bytesarr = new byte[4096];
				byte[] bytes = new byte[4096];
				/*
				 * int c; int i = 0; while((c = in.read(bytesarr))!= -1) { bytes[i] =
				 * (byte)in.(); i++; }
				 */
				int i = 0;
				for(; 0<(i = in.read(bytesarr)); ){
					bytearr.write(bytesarr,0,i);
				}
				System.out.println("byte size: "+i);
				bytes = bytearr.toByteArray();
				
				bytearr.close();
				in.close();
				
				return bytes;
			}
        }catch(Exception e) {
        	System.out.println("Error:"+e);
        	return null;
        }
		/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
		  is = url.openStream ();
		  byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
		  int n;

		  while ( (n = is.read(byteChunk)) > 0 ) {
		    baos.write(byteChunk, 0, n);
		  }
		  return(byteChunk);
		} 
		catch (IOException e) {
		  System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
		  e.printStackTrace ();
		  return null;
		  // Perform any other exception handling that's appropriate.
		}
		finally {
		  if (is != null) { is.close(); }
		  
		}*/
        /*BufferedImage bi = ImageIO.read(new File(url));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        return bytes;*/
    }
    private static List<EntityAnnotation> getImageLabels(String imageUrl) {
        try {
            byte[] imgBytes = downloadFile(new URL(imageUrl));
        	//byte[] imgBytes = downloadFile(imageUrl);
            ByteString byteString = ByteString.copyFrom(imgBytes);
            Image image = Image.newBuilder().setContent(byteString).build();
        	
            Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
            List<AnnotateImageRequest> requests = new ArrayList<>();
            requests.add(request);
            ImageAnnotatorClient client = ImageAnnotatorClient.create();
            BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
            client.close();
            List<AnnotateImageResponse> imageResponses = batchResponse.getResponsesList();
            AnnotateImageResponse imageResponse = imageResponses.get(0);
            if (imageResponse.hasError()) {
                System.err.println("Error getting image labels: " + imageResponse.getError().getMessage());
                return null;
            }
            return imageResponse.getLabelAnnotationsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
