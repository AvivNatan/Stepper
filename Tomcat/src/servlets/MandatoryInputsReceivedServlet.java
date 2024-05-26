package servlets;

import com.google.gson.Gson;
import dto.DtoFreeInputs;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "MandatoryInputsReceivedServlet", urlPatterns = "/mandatory_inputs")
public class MandatoryInputsReceivedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();
            String userName = req.getParameter("username");
            boolean result = system.getOptionalExecution(userName).isAllMandatoryInputsReceived();
            String resultJSON = gson.toJson(result);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);



    }
}

