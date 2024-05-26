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

@WebServlet(name = "GetTypeOfFreeInputServlet", urlPatterns = "/type_free_input")
public class GetTypeOfFreeInputServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();

            String userName = req.getParameter("username");
            String inputName=req.getParameter("inputname");
            FlowExecution flow=system.getOptionalExecution(userName);
            DataInFlow data=flow.getFreeInputs().get(inputName);
            String type = data.getDataUsageDefinition().getDataDefinition().dataDefinition().getType().getSimpleName();
            Object obj=data.getContent();
            Object moreInfo=data.getDataUsageDefinition().getDataDefinition().getMoreInfoOfData();
            DtoInputFillingResponse result=new DtoInputFillingResponse(obj,type,moreInfo);

            String resultJSON = gson.toJson(result);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(resultJSON);
        }



}
