package es.ubu.ecosystemIA.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class FileBaseController {
	public static final String PARAM_BASE_URL = "baseURL";
    
    //get base URL
    public String getBaseURL(HttpServletRequest request){
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    	//ServletContext context = request.getSession().getServletContext();
        //String realContextPath = context.getRealPath(request.getContextPath());
        //return realContextPath;
    }
}
