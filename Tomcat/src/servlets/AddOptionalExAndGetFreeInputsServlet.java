package servlets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.DtoFreeInputDescription;
import dto.DtoFreeInputOptionalExResponse;
import dto.DtoFreeInputs;
import dto.DtoFreeInputsOptionalExResponse;
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

@WebServlet(name = "AddOptionalExAndGetFreeInputsServlet", urlPatterns = "/add_optionalEx_and_inputs")
public class AddOptionalExAndGetFreeInputsServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

            EngineSystem system = ServletUtils.getSystemManager(getServletContext());
            Gson gson = new Gson();
            FlowExecution optionalExecution;
            String userName = req.getParameter("username");
            String nameFlow=req.getParameter("nameflow");
            String isConCurrEx=req.getParameter("iscontinuecurrex");
            UUID idContinue;
            if(isConCurrEx.equals("true"))
            {
                FlowExecution curr=system.getOptionalExecution(userName);
                idContinue=curr.getUniqueId();
            }
            else
            {
                if(req.getParameter("idcontinue").isEmpty())
                    idContinue=null;
                else
                    idContinue=UUID.fromString(req.getParameter("idcontinue"));
            }


            if(!req.getParameter("idrexecute").isEmpty())
            {
                FlowExecution reExecution=system.
                        findActivatedExecutionByID(UUID.fromString(req.getParameter("idrexecute")));
                nameFlow=reExecution.getFlowDefinition().getName();

                optionalExecution=new FlowExecution(reExecution.getFlowDefinition(),
                        system.findActivatedExecutionByID(idContinue),userName);
                optionalExecution.copyFreeInputs(reExecution);
            }
            else
            {
                optionalExecution=new FlowExecution(system.
                        getFlowDefinitionByName(nameFlow,system.getAllFlowsAllFiles()),
                        system.findActivatedExecutionByID(idContinue),userName);
            }
            system.addOptionalExecution(userName,optionalExecution);
            DtoFreeInputs list=system.getFreeInputsByFlowName(nameFlow);
            DtoFreeInputsOptionalExResponse result=new DtoFreeInputsOptionalExResponse();
            for(DtoFreeInputDescription dto:list.getFreeInputs())
            {
                result.getList().add(new DtoFreeInputOptionalExResponse(dto.getFinalName(),
                        dto.getNecessity()));
            }
            String freeInputsJSON = gson.toJson(result);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write(freeInputsJSON);


    }
}
