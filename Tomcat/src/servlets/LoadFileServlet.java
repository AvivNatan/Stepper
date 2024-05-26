package servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import stepper.engine.EngineSystem;
import stepper.roles.RoleManager;
import stepper.users.UserManager;
import utils.ServletUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "LoadFileServlet", urlPatterns = "/load_file")
public class LoadFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/plain");
        String path=request.getParameter("path");
        Collection<Part> parts = request.getParts();

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            part.getSubmittedFileName();
            //to write the content of the file to a string
            fileContent.append(readFromInputStream(part.getInputStream()));
        }
        EngineSystem system = ServletUtils.getSystemManager(getServletContext());
        RoleManager roleManager = ServletUtils.getRolesManager(getServletContext());
        if (!path.endsWith(".xml"))
        {
            response.getWriter().write("Invalid file type. The file must be an XML file.");
            return;
        }
        // Convert the StringBuilder to a byte array
        byte[] byteArray = fileContent.toString().getBytes();

        // Create an InputStream from the byte array
        InputStream inputStreamFile = new ByteArrayInputStream(byteArray);
        try {
            response.getWriter().write(system.LoadXMLFile(inputStreamFile));
            system.updateRolesAndUsers(roleManager);

        } catch (RuntimeException e) {
            response.getWriter().write(e.getMessage());
        }
    }
    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}

