/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.web;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class MimeTypeMap {

    private Map<String, String> mimeTypeToExtensionMap = new TreeMap<>();
    private Map<String, String> extensionToMimeTypeMap = new TreeMap<>();

    private MimeTypeMap() {
        initialize();
    }

    private static class MimeTypeMapHolder {

        public static final MimeTypeMap INSTANCE = new MimeTypeMap();
    }

    public static MimeTypeMap getInstance() {
        return MimeTypeMapHolder.INSTANCE;
    }

    public String getMimeTypeForExtension(String extension) {
        return extensionToMimeTypeMap.get(extension);
    }

    public boolean hasMimeType(String extension) {
        try {
            return extensionToMimeTypeMap.containsKey(extension);
        }
        catch (NullPointerException npex) {
            return false;
        }
    }

    public String getExtensionForMimeType(String mimeType) {
        return mimeTypeToExtensionMap.get(mimeType);
    }

    public boolean hasExtension(String mimeType) {
        try {
            return mimeTypeToExtensionMap.containsKey(mimeType);
        }
        catch (NullPointerException npex) {
            return false;
        }
    }

    private void insertEntry(String mimeType, String extension) {
        if (!mimeTypeToExtensionMap.containsKey(mimeType)) {
            mimeTypeToExtensionMap.put(mimeType, extension);
        }
        extensionToMimeTypeMap.put(extension, mimeType);
    }

    private void initialize() {
        insertEntry("application/andrew-inset", "ez");
        insertEntry("application/dsptype", "tsp");
        insertEntry("application/futuresplash", "spl");
        insertEntry("application/hta", "hta");
        insertEntry("application/mac-binhex40", "hqx");
        insertEntry("application/mac-compactpro", "cpt");
        insertEntry("application/mathematica", "nb");
        insertEntry("application/msaccess", "mdb");
        insertEntry("application/oda", "oda");
        insertEntry("application/ogg", "ogg");
        insertEntry("application/pdf", "pdf");
        insertEntry("application/pgp-keys", "key");
        insertEntry("application/pgp-signature", "pgp");
        insertEntry("application/pics-rules", "prf");
        insertEntry("application/rar", "rar");
        insertEntry("application/rdf+xml", "rdf");
        insertEntry("application/rss+xml", "rss");
        insertEntry("application/zip", "zip");
        insertEntry("application/vnd.android.package-archive", "apk");
        insertEntry("application/vnd.cinderella", "cdy");
        insertEntry("application/vnd.ms-pki.stl", "stl");
        insertEntry("application/vnd.oasis.opendocument.database", "odb");
        insertEntry("application/vnd.oasis.opendocument.formula", "odf");
        insertEntry("application/vnd.oasis.opendocument.graphics", "odg");
        insertEntry("application/vnd.oasis.opendocument.graphics-template", "otg");
        insertEntry("application/vnd.oasis.opendocument.image", "odi");
        insertEntry("application/vnd.oasis.opendocument.spreadsheet", "ods");
        insertEntry("application/vnd.oasis.opendocument.spreadsheet-template", "ots");
        insertEntry("application/vnd.oasis.opendocument.text", "odt");
        insertEntry("application/vnd.oasis.opendocument.text-master", "odm");
        insertEntry("application/vnd.oasis.opendocument.text-template", "ott");
        insertEntry("application/vnd.oasis.opendocument.text-web", "oth");
        insertEntry("application/msword", "doc");
        insertEntry("application/msword", "dot");
        insertEntry("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        insertEntry("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx");
        insertEntry("application/vnd.ms-excel", "xls");
        insertEntry("application/vnd.ms-excel", "xlt");
        insertEntry("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        insertEntry("application/vnd.openxmlformats-officedocument.spreadsheetml.template", "xltx");
        insertEntry("application/vnd.ms-powerpoint", "ppt");
        insertEntry("application/vnd.ms-powerpoint", "pot");
        insertEntry("application/vnd.ms-powerpoint", "pps");
        insertEntry("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        insertEntry("application/vnd.openxmlformats-officedocument.presentationml.template", "potx");
        insertEntry("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx");
        insertEntry("application/vnd.rim.cod", "cod");
        insertEntry("application/vnd.smaf", "mmf");
        insertEntry("application/vnd.stardivision.calc", "sdc");
        insertEntry("application/vnd.stardivision.draw", "sda");
        insertEntry("application/vnd.stardivision.impress", "sdd");
        insertEntry("application/vnd.stardivision.impress", "sdp");
        insertEntry("application/vnd.stardivision.math", "smf");
        insertEntry("application/vnd.stardivision.writer", "sdw");
        insertEntry("application/vnd.stardivision.writer", "vor");
        insertEntry("application/vnd.stardivision.writer-global", "sgl");
        insertEntry("application/vnd.sun.xml.calc", "sxc");
        insertEntry("application/vnd.sun.xml.calc.template", "stc");
        insertEntry("application/vnd.sun.xml.draw", "sxd");
        insertEntry("application/vnd.sun.xml.draw.template", "std");
        insertEntry("application/vnd.sun.xml.impress", "sxi");
        insertEntry("application/vnd.sun.xml.impress.template", "sti");
        insertEntry("application/vnd.sun.xml.math", "sxm");
        insertEntry("application/vnd.sun.xml.writer", "sxw");
        insertEntry("application/vnd.sun.xml.writer.global", "sxg");
        insertEntry("application/vnd.sun.xml.writer.template", "stw");
        insertEntry("application/vnd.visio", "vsd");
        insertEntry("application/x-abiword", "abw");
        insertEntry("application/x-apple-diskimage", "dmg");
        insertEntry("application/x-bcpio", "bcpio");
        insertEntry("application/x-bittorrent", "torrent");
        insertEntry("application/x-cdf", "cdf");
        insertEntry("application/x-cdlink", "vcd");
        insertEntry("application/x-chess-pgn", "pgn");
        insertEntry("application/x-cpio", "cpio");
        insertEntry("application/x-debian-package", "deb");
        insertEntry("application/x-debian-package", "udeb");
        insertEntry("application/x-director", "dcr");
        insertEntry("application/x-director", "dir");
        insertEntry("application/x-director", "dxr");
        insertEntry("application/x-dms", "dms");
        insertEntry("application/x-doom", "wad");
        insertEntry("application/x-dvi", "dvi");
        insertEntry("application/x-flac", "flac");
        insertEntry("application/x-font", "pfa");
        insertEntry("application/x-font", "pfb");
        insertEntry("application/x-font", "gsf");
        insertEntry("application/x-font", "pcf");
        insertEntry("application/x-font", "pcf.Z");
        insertEntry("application/x-freemind", "mm");
        insertEntry("application/x-futuresplash", "spl");
        insertEntry("application/x-gnumeric", "gnumeric");
        insertEntry("application/x-go-sgf", "sgf");
        insertEntry("application/x-graphing-calculator", "gcf");
        insertEntry("application/x-gtar", "gtar");
        insertEntry("application/x-gtar", "tgz");
        insertEntry("application/x-gtar", "taz");
        insertEntry("application/x-hdf", "hdf");
        insertEntry("application/x-ica", "ica");
        insertEntry("application/x-internet-signup", "ins");
        insertEntry("application/x-internet-signup", "isp");
        insertEntry("application/x-iphone", "iii");
        insertEntry("application/x-iso0-image", "iso");
        insertEntry("application/x-jmol", "jmz");
        insertEntry("application/x-kchart", "chrt");
        insertEntry("application/x-killustrator", "kil");
        insertEntry("application/x-koan", "skp");
        insertEntry("application/x-koan", "skd");
        insertEntry("application/x-koan", "skt");
        insertEntry("application/x-koan", "skm");
        insertEntry("application/x-kpresenter", "kpr");
        insertEntry("application/x-kpresenter", "kpt");
        insertEntry("application/x-kspread", "ksp");
        insertEntry("application/x-kword", "kwd");
        insertEntry("application/x-kword", "kwt");
        insertEntry("application/x-latex", "latex");
        insertEntry("application/x-lha", "lha");
        insertEntry("application/x-lzh", "lzh");
        insertEntry("application/x-lzx", "lzx");
        insertEntry("application/x-maker", "frm");
        insertEntry("application/x-maker", "maker");
        insertEntry("application/x-maker", "frame");
        insertEntry("application/x-maker", "fb");
        insertEntry("application/x-maker", "book");
        insertEntry("application/x-maker", "fbdoc");
        insertEntry("application/x-mif", "mif");
        insertEntry("application/x-ms-wmd", "wmd");
        insertEntry("application/x-ms-wmz", "wmz");
        insertEntry("application/x-msi", "msi");
        insertEntry("application/x-ns-proxy-autoconfig", "pac");
        insertEntry("application/x-nwc", "nwc");
        insertEntry("application/x-object", "o");
        insertEntry("application/x-oz-application", "oza");
        insertEntry("application/x-pkcs12", "p12");
        insertEntry("application/x-pkcs7-certreqresp", "p7r");
        insertEntry("application/x-pkcs7-crl", "crl");
        insertEntry("application/x-quicktimeplayer", "qtl");
        insertEntry("application/x-shar", "shar");
        insertEntry("application/x-shockwave-flash", "swf");
        insertEntry("application/x-stuffit", "sit");
        insertEntry("application/x-sv4cpio", "sv4cpio");
        insertEntry("application/x-sv4crc", "sv4crc");
        insertEntry("application/x-tar", "tar");
        insertEntry("application/x-texinfo", "texinfo");
        insertEntry("application/x-texinfo", "texi");
        insertEntry("application/x-troff", "t");
        insertEntry("application/x-troff", "roff");
        insertEntry("application/x-troff-man", "man");
        insertEntry("application/x-ustar", "ustar");
        insertEntry("application/x-wais-source", "src");
        insertEntry("application/x-wingz", "wz");
        insertEntry("application/x-webarchive", "webarchive");
        insertEntry("application/x-x-ca-cert", "crt");
        insertEntry("application/x-x-user-cert", "crt");
        insertEntry("application/x-xcf", "xcf");
        insertEntry("application/x-xfig", "fig");
        insertEntry("application/xhtml+xml", "xhtml");
        insertEntry("audio/3gpp", "3gpp");
        insertEntry("audio/basic", "snd");
        insertEntry("audio/midi", "mid");
        insertEntry("audio/midi", "midi");
        insertEntry("audio/midi", "kar");
        insertEntry("audio/mpeg", "mpga");
        insertEntry("audio/mpeg", "mpega");
        insertEntry("audio/mpeg", "mp2");
        insertEntry("audio/mpeg", "mp3");
        insertEntry("audio/mpeg", "m4a");
        insertEntry("audio/mpegurl", "m3u");
        insertEntry("audio/prs.sid", "sid");
        insertEntry("audio/x-aiff", "aif");
        insertEntry("audio/x-aiff", "aiff");
        insertEntry("audio/x-aiff", "aifc");
        insertEntry("audio/x-gsm", "gsm");
        insertEntry("audio/x-mpegurl", "m3u");
        insertEntry("audio/x-ms-wma", "wma");
        insertEntry("audio/x-ms-wax", "wax");
        insertEntry("audio/x-pn-realaudio", "ra");
        insertEntry("audio/x-pn-realaudio", "rm");
        insertEntry("audio/x-pn-realaudio", "ram");
        insertEntry("audio/x-realaudio", "ra");
        insertEntry("audio/x-scpls", "pls");
        insertEntry("audio/x-sd2", "sd2");
        insertEntry("audio/x-wav", "wav");
        insertEntry("image/bmp", "bmp");
        insertEntry("image/gif", "gif");
        insertEntry("image/ico", "cur");
        insertEntry("image/ico", "ico");
        insertEntry("image/ief", "ief");
        insertEntry("image/jpeg", "jpeg");
        insertEntry("image/jpeg", "jpg");
        insertEntry("image/jpeg", "jpe");
        insertEntry("image/pcx", "pcx");
        insertEntry("image/png", "png");
        insertEntry("image/svg+xml", "svg");
        insertEntry("image/svg+xml", "svgz");
        insertEntry("image/tiff", "tiff");
        insertEntry("image/tiff", "tif");
        insertEntry("image/vnd.djvu", "djvu");
        insertEntry("image/vnd.djvu", "djv");
        insertEntry("image/vnd.wap.wbmp", "wbmp");
        insertEntry("image/x-cmu-raster", "ras");
        insertEntry("image/x-coreldraw", "cdr");
        insertEntry("image/x-coreldrawpattern", "pat");
        insertEntry("image/x-coreldrawtemplate", "cdt");
        insertEntry("image/x-corelphotopaint", "cpt");
        insertEntry("image/x-icon", "ico");
        insertEntry("image/x-jg", "art");
        insertEntry("image/x-jng", "jng");
        insertEntry("image/x-ms-bmp", "bmp");
        insertEntry("image/x-photoshop", "psd");
        insertEntry("image/x-portable-anymap", "pnm");
        insertEntry("image/x-portable-bitmap", "pbm");
        insertEntry("image/x-portable-graymap", "pgm");
        insertEntry("image/x-portable-pixmap", "ppm");
        insertEntry("image/x-rgb", "rgb");
        insertEntry("image/x-xbitmap", "xbm");
        insertEntry("image/x-xpixmap", "xpm");
        insertEntry("image/x-xwindowdump", "xwd");
        insertEntry("model/iges", "igs");
        insertEntry("model/iges", "iges");
        insertEntry("model/mesh", "msh");
        insertEntry("model/mesh", "mesh");
        insertEntry("model/mesh", "silo");
        insertEntry("text/calendar", "ics");
        insertEntry("text/calendar", "icz");
        insertEntry("text/comma-separated-values", "csv");
        insertEntry("text/css", "css");
        insertEntry("text/html", "htm");
        insertEntry("text/html", "html");
        insertEntry("text/h", "");
        insertEntry("text/iuls", "uls");
        insertEntry("text/mathml", "mml");
        // add it first so it will be the default for ExtensionFromMimeType
        insertEntry("text/plain", "txt");
        insertEntry("text/plain", "asc");
        insertEntry("text/plain", "text");
        insertEntry("text/plain", "diff");
        insertEntry("text/plain", "po");     // reserve "pot" for vnd.ms-powerpoint
        insertEntry("text/richtext", "rtx");
        insertEntry("text/rtf", "rtf");
        insertEntry("text/texmacs", "ts");
        insertEntry("text/text", "phps");
        insertEntry("text/tab-separated-values", "tsv");
        insertEntry("text/xml", "xml");
        insertEntry("text/x-bibtex", "bib");
        insertEntry("text/x-boo", "boo");
        insertEntry("text/x-c++hdr", "h++");
        insertEntry("text/x-c++hdr", "hpp");
        insertEntry("text/x-c++hdr", "hxx");
        insertEntry("text/x-c++hdr", "hh");
        insertEntry("text/x-c++src", "c++");
        insertEntry("text/x-c++src", "cpp");
        insertEntry("text/x-c++src", "cxx");
        insertEntry("text/x-chdr", "h");
        insertEntry("text/x-component", "htc");
        insertEntry("text/x-csh", "csh");
        insertEntry("text/x-csrc", "c");
        insertEntry("text/x-dsrc", "d");
        insertEntry("text/x-haskell", "hs");
        insertEntry("text/x-java", "java");
        insertEntry("text/x-literate-haskell", "lhs");
        insertEntry("text/x-moc", "moc");
        insertEntry("text/x-pascal", "p");
        insertEntry("text/x-pascal", "pas");
        insertEntry("text/x-pcs-gcd", "gcd");
        insertEntry("text/x-setext", "etx");
        insertEntry("text/x-tcl", "tcl");
        insertEntry("text/x-tex", "tex");
        insertEntry("text/x-tex", "ltx");
        insertEntry("text/x-tex", "sty");
        insertEntry("text/x-tex", "cls");
        insertEntry("text/x-vcalendar", "vcs");
        insertEntry("text/x-vcard", "vcf");
        insertEntry("video/3gpp", "3gpp");
        insertEntry("video/3gpp", "3gp");
        insertEntry("video/3gpp", "3g2");
        insertEntry("video/dl", "dl");
        insertEntry("video/dv", "dif");
        insertEntry("video/dv", "dv");
        insertEntry("video/fli", "fli");
        insertEntry("video/m4v", "m4v");
        insertEntry("video/mpeg", "mpeg");
        insertEntry("video/mpeg", "mpg");
        insertEntry("video/mpeg", "mpe");
        insertEntry("video/mp4", "mp4");
        insertEntry("video/mpeg", "VOB");
        insertEntry("video/quicktime", "qt");
        insertEntry("video/quicktime", "mov");
        insertEntry("video/vnd.mpegurl", "mxu");
        insertEntry("video/x-la-asf", "lsf");
        insertEntry("video/x-la-asf", "lsx");
        insertEntry("video/x-mng", "mng");
        insertEntry("video/x-ms-asf", "asf");
        insertEntry("video/x-ms-asf", "asx");
        insertEntry("video/x-ms-wm", "wm");
        insertEntry("video/x-ms-wmv", "wmv");
        insertEntry("video/x-ms-wmx", "wmx");
        insertEntry("video/x-ms-wvx", "wvx");
        insertEntry("video/x-msvideo", "avi");
        insertEntry("video/x-sgi-movie", "movie");
        insertEntry("x-conference/x-cooltalk", "ice");
        insertEntry("x-epoc/x-sisx-app", "sisx");
    }
}
