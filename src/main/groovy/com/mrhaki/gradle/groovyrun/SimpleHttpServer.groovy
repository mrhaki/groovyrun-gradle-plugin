package com.mrhaki.gradle.groovyrun

import groovy.transform.CompileStatic

@CompileStatic
class SimpleHttpServer extends GroovyRun {
    
    private static final Integer DEFAULT_PORT = 8080

    @Override
    void exec() {
        if (!listenerPort) {
            listenerPort = DEFAULT_PORT
        }
        
        evaluate '''\
        // init variable is true before
        // the first client request, so
        // the following code is executed once.
        if (init) {
            headers = [:]
            binaryTypes = ["gif","jpg","png"]
            mimeTypes = [
                "css"  : "text/css",
                "gif"  : "image/gif",
                "htm"  : "text/html",
                "html" : "text/html",
                "jpg"  : "image/jpeg",
                "png"  : "image/png",
                "svg"  : "image/svg+xml",
                "ttf"  : "application/x-font-ttf",
                "otf"  : "application/x-font-opentype",
                "woff" : "application/font-woff",
                "woff2": "application/font-woff2",
                "eot"  : "application/vnd.ms-fontobject",
                "sfnt" : "application/font-sfnt" 
            ]
            baseDir = System.properties['baseDir'] ?: '.\'
        }
        
        // parse the request
        if (line.toLowerCase().startsWith("get")) {
            content = line.tokenize()[1]
        } else {
            def h = line.tokenize(":")
            headers[h[0]] = h[1]
        }
        
        // all done, now process request
        if (line.size() == 0) {
            processRequest()
            return "success"
        }
        
        def processRequest() {
            if (content.indexOf("..") < 0) { //simplistic security
                // simple file browser rooted from current dir
                def file = new File(new File(baseDir), content)
                if (file.isDirectory()) {
                    printDirectoryListing(file)
                } else {
                    extension = content.substring(content.lastIndexOf(".") + 1)
                    printHeaders(mimeTypes.get(extension,"text/plain"))
        
                    if (binaryTypes.contains(extension)) {
                        socket.outputStream.write(file.readBytes())
                    } else {
                        println(file.text)
                    }
                }
            }
        }
        
        def printDirectoryListing(dir) {
            printHeaders("text/html")
            println "<html><head></head><body>"
            for (file in dir.list().toList().sort()) {
                // special case for root document
                if ("/" == content) {
                    content = ""
                }
                println "<li><a href='${content}/${file}'>${file}</a></li>"
            }
            println "</body></html>"
        }
        
        def printHeaders(mimeType) {
            println "HTTP/1.0 200 OK"
            println "Content-Type: ${mimeType}"
            println ""
        }
        '''.stripMargin()
        
        super.exec()
    }

    void baseDir(final File dir) {
        systemProperty 'baseDir', dir.absolutePath
    }
    
    void setBaseDir(final File dir) {
        baseDir(dir)
    }
}
