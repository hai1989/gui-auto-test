package util;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import java.text.SimpleDateFormat;


public class FormatHTMLLayout extends HTMLLayout {

    public FormatHTMLLayout() {
    }

    protected final int BUF_SIZE = 256;

    protected final int MAX_CAPACITY = 1024;

    static String TRACE_PREFIX = "<br>    ";

    private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

    String title = "test";

    /**
     * A string baidu.constant used in naming the option for setting the the HTML
     * document title. Current value of this string baidu.constant is <b>Title</b>.
     */
    public static final String TITLE_OPTION = "Title";

    // Print no location info by default
    boolean locationInfo = true;

    public String format(LoggingEvent event)
    {
        if (sbuf.capacity() > MAX_CAPACITY) {
            sbuf = new StringBuffer(BUF_SIZE);
        } else {
            sbuf.setLength(0);
        }

        if (event.getLevel().equals(Level.FATAL) || event.getLevel().equals(Level.ERROR)) {
            sbuf.append("<tr td bgcolor=\"red\" style=\"font-size : xx-small;\" colspan=\"4\">");
        } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
            sbuf.append("<tr  bgcolor=\"Yellow\" style=\"font-size : xx-small;\" colspan=\"4\">");
        } else {
            sbuf.append("<tr bgcolor=\"white\" style=\"font-size : xx-small;\" colspan=\"4\">");
        }

        sbuf.append("<td title=\"执行时间\">");
        sbuf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        sbuf.append("</td>" + Layout.LINE_SEP);

        sbuf.append("<td title=\"级别\">");
        if (event.getLevel().equals(Level.FATAL)) {
            sbuf.append("<font color=\"#339933\">");
            sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            sbuf.append("</font>");
        } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
            sbuf.append("<font color=\"#993300\"><strong>");
            sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            sbuf.append("</strong></font>");
        } else {
            sbuf.append("<font color=\"green\">");
            sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
            sbuf.append("</font>");
        }
        sbuf.append("</td>" + Layout.LINE_SEP);

        if (locationInfo) {
            LocationInfo locInfo = event.getLocationInformation();
            sbuf.append("<td title=\"所在行\">");
            String classname = Transform.escapeTags(locInfo.getFileName());
            String methodname = Transform.escapeTags(locInfo.getMethodName());
            int line = classname.indexOf(".");
            sbuf.append(classname.substring(0,line)+"."+methodname+":");
            sbuf.append(locInfo.getLineNumber());
            sbuf.append("</td>" + Layout.LINE_SEP);
        }

        sbuf.append("<td title=\"log信息\">");
        sbuf.append(Transform.escapeTags(event.getRenderedMessage()));
        sbuf.append("</td>" + Layout.LINE_SEP);
        sbuf.append("</tr>" + Layout.LINE_SEP);

        if (event.getNDC() != null) {
            sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : xx-small;\" colspan=\"6\" title=\"Nested Diagnostic Context\">");
            sbuf.append("NDC: " + Transform.escapeTags(event.getNDC()));
            sbuf.append("</td></tr>" + Layout.LINE_SEP);
        }

        String[] s = event.getThrowableStrRep();
        if (s != null) {
            if (event.getLevel().equals(Level.FATAL) || event.getLevel().equals(Level.ERROR)) {
                sbuf.append("<tr><td bgcolor=\"#339933\" style=\"font-size : xx-small;\" colspan=\"4\">");
            } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
                sbuf.append("<tr><td bgcolor=\"#993300\" style=\"font-size : xx-small;\" colspan=\"4\">");
            } else {
                sbuf.append("<tr><td bgcolor=\"green\" style=\"font-size : xx-small;\" colspan=\"4\">");
            }
            appendThrowableAsHTML(s, sbuf);
            sbuf.append("</td></tr>" + Layout.LINE_SEP);
        }
        return sbuf.toString();
    }
    private void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
        if (s != null) {
            int len = s.length;
            if (len == 0)
                return;
            sbuf.append(Transform.escapeTags(s[0]));
            sbuf.append(Layout.LINE_SEP);
            for (int i = 1; i < len; i++) {
                sbuf.append(TRACE_PREFIX);
                sbuf.append(Transform.escapeTags(s[i]));
                sbuf.append(Layout.LINE_SEP);
            }
        }
    }

    /**
     * Returns appropriate HTML headers.
     */
    public String getHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + Layout.LINE_SEP);
        sbuf.append("<html>" + Layout.LINE_SEP);
        sbuf.append("<head>" + Layout.LINE_SEP);
        sbuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" + Layout.LINE_SEP);
        sbuf.append("<title>" + title + "</title>" + Layout.LINE_SEP);
        sbuf.append("<style type=\"text/css\">" + Layout.LINE_SEP);
        sbuf.append("<!--" + Layout.LINE_SEP);
        sbuf.append("body, table {font-family: '??',arial,sans-serif; font-size: 12px;}" + Layout.LINE_SEP);
        sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}" + Layout.LINE_SEP);
        sbuf.append("-->" + Layout.LINE_SEP);
        sbuf.append("</style>" + Layout.LINE_SEP);
        sbuf.append("</head>" + Layout.LINE_SEP);
        sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">" + Layout.LINE_SEP);

        sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">" + Layout.LINE_SEP);
        sbuf.append("<tr>" + Layout.LINE_SEP);

        sbuf.append("<th>执行时间</th>" + Layout.LINE_SEP);
        sbuf.append("<th>级别</th>" + Layout.LINE_SEP);

        if (locationInfo) {
            sbuf.append("<th>所在行</th>" + Layout.LINE_SEP);
        }

        sbuf.append("<th>log信息</th>" + Layout.LINE_SEP);
        sbuf.append("</tr>" + Layout.LINE_SEP);
        sbuf.append("<br></br>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

}
