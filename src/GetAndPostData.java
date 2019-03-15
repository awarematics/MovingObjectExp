
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GetAndPostData extends HttpServlet {
	public static String responseLayer;
	 protected void processRequest(HttpServletRequest request,
	            HttpServletResponse response, String method)
	            throws ServletException, IOException {
	                //把响应内容类型设置为 text/xml
	                response.setContentType("text/xml");
	                //得到用户参数值
	                String order = request.getParameter("order");
	                responseLayer = order;
	             //  System.out.println("this is\t\t"+order);
	              
					String responseText="";
            		//responseText = firstName;
					if(order !=null)
						responseText = order;
					else
						responseText = "Upload file is NULL!!";

	                PrintWriter out = response.getWriter();
	                out.println(responseText);;
	                out.close();
	                
	            }
	            protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	                //Get主函数调用processRequest,完成Get方法的参数接受，返回的过程
	            	
	                processRequest(request, response, "GET");
	            }
	            protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	            	
	                //POST主函数调用processRequest,完成POST方法的参数接受，返回的过程
	                processRequest(request, response, "POST");
	            }
}