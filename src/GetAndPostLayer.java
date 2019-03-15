
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class GetAndPostLayer extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
			throws ServletException, IOException {

		response.setContentType("text/xml");

		String temp = GetAndPostData.responseLayer;
		String result = "";
		if(temp !=null){
		String[] sss = temp.split(" ");
		
		//System.out.println("length\t\t"+sss.length);
		for (int i = 3; i < sss.length-1; i++) {
			temp = "D://mfs/" + sss[i].replace("\"", "");
		//	System.out.println("root\t\t"+temp);
			try {
				BufferedReader in = new BufferedReader(new FileReader(temp));
				String str;
				while ((str = in.readLine()) != null) {
					result += str;
				}
				// System.out.println(str);
			} catch (IOException e) {
			}
			result +="@";
		}
		}
	//	System.out.println(result+"\nresult");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.close();
		//}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get主函数调用processRequest,完成Get方法的参数接受，返回的过程

		processRequest(request, response, "GET");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// POST主函数调用processRequest,完成POST方法的参数接受，返回的过程
		processRequest(request, response, "POST");
	}
}