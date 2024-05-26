package servlets;

import com.google.gson.Gson;
import dto.DtoFlowsDescriptions;
import dto.DtoFlowsNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "GetAllFlowsNameNewRoleServlet", urlPatterns = "/get_flows_new_role")
public class GetAllFlowsNameNewRoleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        EngineSystem system = ServletUtils.getSystemManager(getServletContext());
        Gson gson = new Gson();
        DtoFlowsNames flowsNames = system.getFlowsNames();
        String flowsJSON = gson.toJson(flowsNames);
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write(flowsJSON);

    }

}


