package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "ExecutionsRefresherServlet", urlPatterns = "/executions_refresher")
public class ExecutionsRefresherServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();
            int count=0;
            for(FlowExecution flow:system.getAllFlowsAllFiles().getActivatedFlows())
            {
                if(flow.getFlowExecutionResult()!=null)
                    count++;
            }
            Integer num=count;
            String resultJSON = gson.toJson(num);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);
    }
}
