package servlets;

import com.google.gson.Gson;
import dto.DtoInputFillingResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import stepper.engine.EngineSystem;
import stepper.flow.excution.FlowExecution;
import stepper.flow.excution.context.DataInFlow;
import utils.ServletUtils;

import java.io.IOException;
@WebServlet(name = "UpdateFreeInputContentServlet", urlPatterns = "/update_input_content")
public class UpdateFreeInputContentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


            EngineSystem system = ServletUtils.getSystemManager(getServletContext());

            String userName = req.getParameter("username");
            String inputName = req.getParameter("inputname");
            String content=req.getParameter("content");
            FlowExecution flow = system.getOptionalExecution(userName);
            flow.updateFreeInput(inputName,content);
            res.setStatus(HttpServletResponse.SC_OK);

    }

}

