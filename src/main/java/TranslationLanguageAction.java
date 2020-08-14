package main.java;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.File;
import java.io.RandomAccessFile;

public class TranslationLanguageAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project == null) return;
        String stringKey = Messages.showInputDialog(project, "输入Key", "Key", Messages.getQuestionIcon());
        if (stringKey == null || stringKey.length() <= 0) {
            return;
        }
        String stringCn = Messages.showInputDialog(project, "输入中文", "中文", Messages.getQuestionIcon());
        if (stringCn == null || stringCn.isEmpty()) {
            return;
        }
        String stringCh = Messages.showInputDialog(project, "输入繁体", "中文繁体", Messages.getQuestionIcon());
        if (stringCh == null || stringCh.isEmpty()) {
            return;
        }
        String stringEn = Messages.showInputDialog(project, "输入英文", "英文", Messages.getQuestionIcon());
        if (stringEn == null || stringEn.isEmpty()) {
            return;
        }
        String stringVn = Messages.showInputDialog(project, "输入越南语", "越南语", Messages.getQuestionIcon());
        if (stringVn == null || stringVn.isEmpty()) {
            return;
        }
        String stringMy = Messages.showInputDialog(project, "输入马来语", "马来语", Messages.getQuestionIcon());
        if (stringMy == null || stringMy.isEmpty()) {
            return;
        }
        try {
            writeString(project, "values", stringKey, stringCn);
            writeString(project, "values-zh-rTW", stringKey, stringCh);
            writeString(project, "values-en-rUS", stringKey, stringEn);
            writeString(project, "values-vi-rVN", stringKey, stringVn);
            writeString(project, "values-en-rMY", stringKey, stringMy);
            Messages.showMessageDialog("操作完成，开心吧！",
                    "提示",
                    Messages.getInformationIcon());
        } catch (Exception error) {
            Messages.showMessageDialog("呃呃，失败了，" + error.getMessage(),
                    "提示",
                    Messages.getInformationIcon());
        }
    }

    private void writeString(Project project, String valueName, String key, String value) throws Exception {
        if (project.getWorkspaceFile() == null) return;
        File xmlFile = new File(project.getBasePath() + "/app/src/main/res/" + valueName + "/strings.xml");
        RandomAccessFile randomFile = new RandomAccessFile(xmlFile, "rw");
        long suffixFilePoint = 0;
        String line;
        String findKey = "<string name=\"" + key + "\">";
        boolean hasKey = false;
        while ((line = randomFile.readLine()) != null) {
            if (hasKey = line.contains(findKey)) {
                break;
            }
            if (line.contains("</resources>")) {
                break;
            }
            suffixFilePoint = randomFile.getFilePointer();
        }
        if (hasKey) {
            Messages.showMessageDialog(valueName + " 的 strings.xml 里面 key 已经存在，无需再生成",
                    "提示",
                    Messages.getInformationIcon());
            return;
        }
        if (suffixFilePoint <= 0) {
            Messages.showMessageDialog("呃呃，失败了,error" + suffixFilePoint,
                    "提示",
                    Messages.getInformationIcon());
            return;
        }
        randomFile.seek(suffixFilePoint);
        String writeContent = "    <string name=\"" +
                key + "\">" +
                value +
                "</string>\n</resources>";
        randomFile.write(writeContent.getBytes());
        randomFile.getFD().sync();
        randomFile.close();
    }
}
