package servlets;

import com.google.gson.Gson;
import dto.DtoFlowsDescriptions;
import dto.DtoFlowsExecutionHistoryResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.users.User;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "GetAllHistoryExecutionsServlet", urlPatterns = "/get_flows_history")
public class GetAllHistoryExecutionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            Gson gson = new Gson();
             User user;
            String userName=req.getParameter("username");
            if(userName!=null)
                user=userManager.getUserByName(userName);
            else
                user=null;

            DtoFlowsExecutionHistoryResponse flows = new DtoFlowsExecutionHistoryResponse();
            flows.getList().addAll(system.getFlowExecutionHistory(user));

            String resJSON = gson.toJson(flows);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resJSON);


    }

}

