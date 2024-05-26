package servlets;

import com.google.gson.Gson;
import dto.DtoCurrentExecutionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import stepper.flow.excution.FlowExecutionResult;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "IsCurrentExecutionServlet", urlPatterns = "/current_execution")
public class IsCurrentExecutionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();
            FlowExecutionResult result = null;
            boolean isResult = false;
            String userName = req.getParameter("username");
            FlowExecution optional = system.getOptionalExecution(userName);
            FlowExecution execution = system.findActivatedExecutionByID(optional.getUniqueId());
            if (execution != null) {
                result = execution.getFlowExecutionResult();
                isResult = true;
            }
            DtoCurrentExecutionResponse dto = new DtoCurrentExecutionResponse(result, isResult);
            String resultJSON = gson.toJson(dto);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);


    }

}

