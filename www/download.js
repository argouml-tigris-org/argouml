function WriteLink() {
    if (navigator.appVersion.indexOf("Win")!=-1) {
        document.write("<a href='http://argouml-downloads.tigris.org/nonav/argouml-0.26/ArgoUML-0.26-setup.exe'  onClick=\"javascript:urchinTracker('DOWNLOAD/ArgoUML-0.26-setup.exe')\">0.26 (Windows)</a>");
    }
    else if (navigator.appVersion.indexOf("Mac")!=-1) {
        document.write("<a href='http://argouml-downloads.tigris.org/nonav/argouml-0.26/ArgoUML-0.26.app.tgz'  onClick=\"javascript:urchinTracker('DOWNLOAD/ArgoUML-0.26.app.tgz')\">0.26 (Mac)</a>");
    }
    else if (window.navigator.userAgent.indexOf("X11")!=-1) {
        document.write("<a href='http://argouml-downloads.tigris.org/nonav/argouml-0.26/ArgoUML-0.26.tar.gz'  onClick=\"javascript:urchinTracker('DOWNLOAD/ArgoUML-0.26.tar.gz')\">0.26 (Linux/BSD)</a>");
    }
    else {
        document.write("<a href='http://argouml-downloads.tigris.org/nonav/argouml-0.26/ArgoUML-0.26.zip'  onClick=\"javascript:urchinTracker('DOWNLOAD/ArgoUML-0.26.zip')\">0.26</a>");
    }
}