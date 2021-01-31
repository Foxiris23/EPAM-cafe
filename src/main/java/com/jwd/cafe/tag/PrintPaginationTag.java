package com.jwd.cafe.tag;

import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class prints a navigation of pagination
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class PrintPaginationTag extends TagSupport {
    private Integer pages;
    private Integer page;
    private String url;

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder stringBuilder = new StringBuilder();
        JspWriter out = pageContext.getOut();
        List<Integer> body = calcPagination();

        if (page > 1) {
            String href = "href='" + url + (page - 1) + "' ";
            stringBuilder.append("<li class='page-item'>");
            stringBuilder.append("<a class='page-link' ");
            stringBuilder.append(href);
            stringBuilder.append("aria-label='Previous'> <span aria-hidden='true'>&laquo; </span> </a>");
            stringBuilder.append("</li>");
        }

        for (Integer number : body) {
            String href = "href='" + url + (number) + "' ";
            String active = (number.equals(page)) ? "active" : "";
            stringBuilder.append("<li class='page-item ");
            if (number.equals(-1)) {
                stringBuilder.append("disabled");
            } else {
                stringBuilder.append(active);
            }
            stringBuilder.append("'>");
            stringBuilder.append("<a class='page-link'");
            if (number.equals(-1)) {
                stringBuilder.append(" aria-disabled='true' href='#'");
            } else {
                stringBuilder.append(href);
            }
            stringBuilder.append(">");
            stringBuilder.append(number.equals(-1) ? "..." : number);
            stringBuilder.append("</a>");
            stringBuilder.append("</li>");
        }

        if (!page.equals(pages) && pages > 1) {
            String href = "href='" + url + (page + 1) + "' ";
            stringBuilder.append("<li class='page-item'>");
            stringBuilder.append("<a class='page-link' ");
            stringBuilder.append(href);
            stringBuilder.append("aria-label='Next'> <span aria-hidden='true'>&raquo; </span> </a>");
            stringBuilder.append("</li>");
        }
        try {
            out.write(stringBuilder.toString());
        } catch (IOException e) {
            log.error("Failed to print pagination", e);
        }
        return SKIP_BODY;
    }

    private List<Integer> calcPagination() {
        List<Integer> body = new ArrayList<>();
        if (pages > 7) {
            List<Integer> head = new ArrayList<>(
                    Arrays.asList((page > 4) ? new Integer[]{1, -1} : new Integer[]{1, 2, 3}));

            List<Integer> tail = new ArrayList<>(
                    Arrays.asList((page < pages - 3) ?
                            new Integer[]{-1, pages} : new Integer[]{pages - 2, pages - 1, pages}));

            List<Integer> bodyBefore = new ArrayList<>(
                    Arrays.asList((page > 4 && page < pages - 1) ?
                            new Integer[]{page - 2, page - 1} : new Integer[]{}));

            List<Integer> bodyAfter = new ArrayList<>(
                    Arrays.asList((page > 2 && page < pages - 3) ?
                            new Integer[]{page + 1, page + 2} : new Integer[]{}));

            body.addAll(head);
            body.addAll(bodyBefore);
            body.addAll(Arrays.asList((page > 3 & page < pages - 2) ? new Integer[]{page} : new Integer[]{}));
            body.addAll(bodyAfter);
            body.addAll(tail);
        } else {
            for (int i = 0; i < pages; i++) {
                body.add(i + 1);
            }
        }
        return body;
    }
}
