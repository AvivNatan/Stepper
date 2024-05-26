package servlets;

import com.google.gson.Gson;
import dto.DtoNewRoleRequest;
import dto.DtoRoleList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import stepper.roles.Role;
import stepper.roles.RoleManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "AddNewRoleServlet", urlPatterns = "/add_new_role")
public class AddNewRoleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String infoDataSaved = req.getReader().lines().collect(Collectors.joining(" "));
        DtoNewRoleRequest dto= new Gson().fromJson(infoDataSaved, DtoNewRoleRequest.class);
        Gson gson = new Gson();
        if(!dto.getRoleNAme().isEmpty()&&!dto.getDescription().isEmpty())
       {
           Role role=new Role(dto.getRoleNAme(),dto.getDescription(),new HashSet<>(dto.getFlows()));
           RoleManager roleManager=ServletUtils.getRolesManager(getServletContext());
           roleManager.addRole(role);
           String result ="Role Created Successfully";
           String resultJSON = gson.toJson(result);
           resp.getWriter().write(resultJSON);
           resp.setStatus(HttpServletResponse.SC_OK);
       }
        else
        {
            String result ="Name Or Description Is Empty";
            String resultJSON = gson.toJson(result);
            resp.getWriter().write(resultJSON);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
