package servlets;

import com.google.gson.Gson;
import dto.DtoActivatedFlowDescription;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "GetHistoryExecutionDetailsServlet", urlPatterns = "/get_history_description")
public class GetHistoryExecutionDetailsServlet extends HttpServlet {

        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

                EngineSystem system = ServletUtils.getSystemManager(getServletContext());
                Gson gson = new Gson();

                UUID ID = UUID.fromString(req.getParameter("id"));
                FlowExecution execution=system.findActivatedExecutionByID(ID);
                DtoActivatedFlowDescription dto=new DtoActivatedFlowDescription(
                        ID,execution.getFlowDefinition().getName(),
                        null);
                String resultJSON = gson.toJson(dto);
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(resultJSON);


        }
}
