
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GetAndPostExample extends HttpServlet {
	String file="";
	 protected void processRequest(HttpServletRequest request,
	            HttpServletResponse response, String method)
	            throws ServletException, IOException {
	                //把响应内容类型设置为 text/xml
	                response.setContentType("text/xml");
	                //得到用户参数值
	                String firstName = request.getParameter("firstName");
	              
	              // System.out.println("file\t\t"+file);
	               if(file=="") {
	            	 //  System.out.println("file is null");
	            	   file = firstName;
	               }
	               else
	               {
	            	   file=file+" "+firstName;
	               }
	               //System.out.println(file);
					String responseText="";
            		//responseText = firstName;
					if(firstName !=null)
						responseText = file;
					else
						responseText = "Upload file is NULL!!";
					
            		
	                //写回浏览器
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