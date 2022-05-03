package com.group5.project1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		/*String searchLabel = request.getParameter("searchLabel");
		PrintWriter op = response.getWriter();
		op.print(searchLabel);*/
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PrintWriter op = response.getWriter();
		String searchLabel = request.getQueryString();
		searchLabel = searchLabel.substring(12, searchLabel.length());
		
		String[] operate = searchLabel.split("\\+");
		for (int i =0; i<operate.length;i++) {
			operate[i] = operate[i].substring(0,1).toUpperCase()+operate[i].substring(1);
		}
		searchLabel = String.join(" ",operate);
		op.print(searchLabel);
		ArrayList<String> listlable = getImageFromStore(request, response, datastore, searchLabel);
		op.println("<ul>"); 
		String [] url = new String[listlable.size()];
		for(String str : listlable) { 
			 op.println("<li>"+str+"</li>"); 
			 }
		for (int i = 0; i< listlable.size();i++) {
			url[i] = listlable.get(i);
		}
		
		op.println("</ul>");
		
		request.setAttribute("urllist", listlable);
		RequestDispatcher dispatcher = 
	             request.getRequestDispatcher("output.jsp");
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String searchLabel = request.getParameter("searchLabel");
		PrintWriter op = response.getWriter();
		op.print(searchLabel);
	}
	private ArrayList<String> getImageFromStore(HttpServletRequest request, HttpServletResponse response, DatastoreService datastore, String imageId) {

        Query query =
                new Query("User_Images")
                        .setFilter(new Query.FilterPredicate("labels", Query.FilterOperator.EQUAL, imageId));
        PreparedQuery pq = datastore.prepare(query);
        List<Entity> results = pq.asList(FetchOptions.Builder.withDefaults());
        ArrayList<String> listlable = new ArrayList<>();
        if(null != results) {
            results.forEach(user_Photo -> {
                
                String image_url=user_Photo.getProperty("image_url").toString();
                listlable.add(image_url);
                
            });
        }
        return listlable;
    }
}
