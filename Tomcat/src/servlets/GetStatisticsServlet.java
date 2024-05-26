package servlets;

import com.google.gson.Gson;
import dto.DtoStatistics;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "GetStatisticsServlet", urlPatterns = "/get_statistics")
public class GetStatisticsServlet extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();
            DtoStatistics dto=system.getStatistics();
            String resultJSON = gson.toJson(dto);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);


    }

}
