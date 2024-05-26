package servlets;

import com.google.gson.Gson;
import dto.DtoFlowsDescriptions;
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

@WebServlet(name = "GetFlowsDefinitionsServlet", urlPatterns = "/get_flows_definitions")
public class GetFlowsDefinitionsServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            String userName=req.getParameter("username");
            Gson gson = new Gson();
            DtoFlowsDescriptions flowsDescriptions=new DtoFlowsDescriptions();
            flowsDescriptions.getFlows().addAll(system.getAllFlowsDescriptions(userName));
            String flowsDescriptionsJSON = gson.toJson(flowsDescriptions);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(flowsDescriptionsJSON);


    }

}
