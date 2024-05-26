package stepper.step.imp;

import stepper.dd.imp.DataDefinitionTypes;
import stepper.dd.imp.EnumeratorDef.EnumeratorData;
import stepper.flow.definition.api.StepUsageDescription;
import stepper.flow.excution.context.DataInFlow;
import stepper.flow.excution.context.DataInFlowImp;
import stepper.flow.excution.context.StepExecutionContext;
import stepper.flow.excution.context.StepsLog;
import stepper.step.api.*;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper extends AbstractStepDefinition implements Serializable
{
    public Zipper()
    {
        super("Zipper", false);
        DataDefinitionDescription data;
        data=new DataDefinitionDescriptionImpl("SOURCE",
                DataNecessity.MANDATORY, "Source", DataDefinitionTypes.STRING);
        data.setMoreInfoOfData(new Boolean(true));
        addInput(data);
        data=new DataDefinitionDescriptionImpl("OPERATION",
                DataNecessity.MANDATORY, "Operation type", DataDefinitionTypes.ENUMERATOR);
        Set<String> list=new HashSet<>();
        list.add("ZIP");
        list.add("UNZIP");
        data.setMoreInfoOfData(list);
        addInput(data);
        addOutput(new DataDefinitionDescriptionImpl("RESULT", DataNecessity.NA,
                "Zip operation result", DataDefinitionTypes.STRING));

    }
    private static void zipDirectory(String sourcePath, String zipFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File fileToZip = new File(sourcePath);
        zipFile(fileToZip, fileToZip.getName(), zipOut);

        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }

            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zipOut.write(buffer, 0, length);
        }
        fis.close();
    }
    private static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = zipIn.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }


    @Override
    public StepResult invoke(StepExecutionContext context) {
        long start = System.currentTimeMillis();
        DataInFlow data;
        StepUsageDescription usage=context.getFlowExecution().getFlowDefinition().
                getStepUsageDescription(context.getFinalStepName());
        String source=context.getDataValue("SOURCE",String.class,this);
        EnumeratorData operationType=context.getDataValue("OPERATION",EnumeratorData.class,this);
        String result;
        if(Objects.equals(operationType.getChosenValue(), "ZIP"))
        {
            try {
                File file=new File(source);
                if(file.exists())
                {
                    context.addLog(new StepsLog("About to perform operation Zip on source "+source));
                    int extensionIndex = source.lastIndexOf(".");
                    String nameWithoutExtension;
                    if (extensionIndex != -1) {
                        nameWithoutExtension = source.substring(0, extensionIndex);
                    } else {
                        nameWithoutExtension = source;
                    }
                    zipDirectory(source, nameWithoutExtension+".zip");

                }
                else
                {
                    context.addLog(new StepsLog("The path dosent exist"));
                    context.addSummeryLine("The path dosent exist");
                    result="FAILURE because path dosent exist";
                    data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),result);
                    context.storeDataValue("RESULT", data);
                    context.updateDurationStep(start);
                    return StepResult.FAILURE;
                }
            } catch (IOException e) {
                context.addLog(new StepsLog("Error occurred while creating the zip file"));
                context.addSummeryLine("Error occurred while creating the zip file");
                result="FAILURE because couldn't create the zip file";
                data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),result);
                context.storeDataValue("RESULT", data);
                context.updateDurationStep(start);
                return StepResult.FAILURE;
            }
        }
        else
        {
            try
            {
                if(source.endsWith(".zip"))
                {
                    context.addLog(new StepsLog("About to perform operation Unzip on source "+source));
                    int extensionIndex = source.lastIndexOf("\\");
                    unzip(source, source.substring(0, extensionIndex));
                }
                else
                {
                    context.addLog(new StepsLog("The source isn't a zip file"));
                    context.addSummeryLine("The source isn't a zip file");
                    result="FAILURE because source isn't a zip file";
                    data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),result);
                    context.storeDataValue("RESULT", data);
                    context.updateDurationStep(start);
                    return StepResult.FAILURE;
                }
            } catch (IOException e) {
                context.addLog(new StepsLog("Error occurred while extracting  the zip file"));
                context.addSummeryLine("Error occurred while extracting the zip file");
                result="FAILURE because couldn't extracting the zip file";
                data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),result);
                context.storeDataValue("RESULT", data);
                context.updateDurationStep(start);
                return StepResult.FAILURE;
            }
        }

        context.addLog(new StepsLog("operation "+operationType.getChosenValue()+" ended successfully"));
        context.addSummeryLine("operation "+operationType.getChosenValue()+" ended successfully");
        result="SUCCESS operation "+operationType.getChosenValue();
        data=new DataInFlowImp(usage.getDataUsageDescriptionByOriginalName("RESULT"),result);
        context.storeDataValue("RESULT", data);
        context.updateDurationStep(start);
        return StepResult.SUCCESS;
    }
}
