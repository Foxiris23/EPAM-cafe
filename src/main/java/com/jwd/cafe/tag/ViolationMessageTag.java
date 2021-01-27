package com.jwd.cafe.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Set;

public class ViolationMessageTag extends TagSupport {

    private Set<String> violationMessages;

    public void setViolationMessages(Set<String> violationMessages) {
        this.violationMessages = violationMessages;
    }

    @Override
    public int doStartTag() throws JspException {
        if (violationMessages != null && violationMessages.size() > 0) {
            String result = String.join("\n", violationMessages);
            JspWriter jspWriter = pageContext.getOut();
            try {
                jspWriter.write(result);
            } catch (IOException e) {
                throw new JspException();
            }
        }
        return SKIP_BODY;
    }
}