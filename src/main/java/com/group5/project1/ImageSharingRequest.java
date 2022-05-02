package com.group5.project1;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Servlet implementation class ImageSharingRequest
 */
@WebServlet("/ImageSharingRequest")
public class ImageSharingRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageSharingRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.print("In Java");
		HttpSession session = request.getSession(true);
		
		String url = (String) session.getAttribute("url");
		String id = (String) session.getAttribute("id");
		
		
	
		String token = (String) request.getParameter("accessToken");
		System.out.print("Token"+token);
		//getImagesInAnAlbum(token);
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		System.out.print("In Java");
	}
	
	public static void getImagesInAnAlbum(String token) throws IOException {

		
        //String token   = "EAAXlxcZC9UVMBAFpNUZCOAQDZAZBZBspd2bbfJAmEZAaFHe7LgtbM0p0v1d8CndKm1AtY73IVxXZAgUUfVv5dZCCZBplgVZAq3pZCDAq4VrVXMZCZCUN6ABkQHtZBgLyQdkpfcd0ksTdIsJNZBv03ZCx0Y9b8mralAtpopyS1TQXgwWNs124xnNYqVou22zXTMTPb8Kgn9L4ZB7I5pWwPbBT3naVjfZABgTFc9e3p2TgqYHZBdlj0JBQZBoZCohg5lv93";
        String command = "curl -X GET -G -d access_token=" + token + " https://graph.facebook.com/v13.0/102189569868570/photos?fields=link,picture";
        
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader stdInput = new BufferedReader(
            new InputStreamReader(process.getInputStream())
        );

        BufferedReader stdError = new BufferedReader(
            new InputStreamReader(process.getErrorStream())
        );

        // Read the output from the command
        System.out.println("Here is the standard output of the command:\n");

        StringBuilder response = new StringBuilder();

        String line;
        while ((line = stdInput.readLine()) != null) {
            response.append(line);
        }

        System.out.println(response.toString());

        JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response.toString());
			JSONArray data        = jsonObject.getJSONArray("data");

	        for (int i = 0; i < data.length(); i++) {

	            JSONObject pictureObject = data.getJSONObject(i);
	            Iterator<String> key     = pictureObject.keys();

	            String facebookLink      = pictureObject.getString("link");
	            String picture           = pictureObject.getString("picture");

	            System.out.println(picture);
	            InputStream in = new URL(picture).openStream();
	            Files.copy(in, Paths.get(i + ".jpg"));
	            
		}
		}catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
            

        }

    }


