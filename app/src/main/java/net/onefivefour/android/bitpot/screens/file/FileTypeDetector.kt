package net.onefivefour.android.bitpot.screens.file

/**
 * Use this object to get the mime type of any known file extension
 */
object FileTypeDetector {
    private val FILENAME_EXTENSION_ARRAY =
        arrayOf(
            arrayOf("APL", "text/apl", "apl"),
            arrayOf("Asterisk dialplan", "text/x-asterisk", "conf"),
            arrayOf("C", "text/x-csrc", "c", "m"),
            arrayOf("C++", "text/x-c++src", "cpp", "cc", "hpp", "hh", "h"),
            arrayOf("C#", "text/x-csharp", "cs"),
            arrayOf("C-Shell", "application/x-csh", "csh"),
            arrayOf("Java", "text/x-java", "java"),
            arrayOf("CLIPS", "application/x-msclip", "clp"),
            arrayOf("Clojure", "text/x-clojure.", "clj", "cljs"),
            arrayOf("COBOL", "text/x-cobol", "cbl"),
            arrayOf("CoffeeScript", "text/x-coffeescript", "coffee"),
            arrayOf("Lisp", "text/x-common-lisp", "lisp", "lsp", "el", "cl", "jl", "L", "emacs", "sawfishrc"),
            arrayOf("CSS", "text/css", "css"),
            arrayOf("Scss", "text/x-scss", "scss"),
            arrayOf("Sass", "text/x-sass", "sass"),
            arrayOf("Less", "text/x-x-less", "less"),
            arrayOf("D", "text/x-d", "d"),
            arrayOf("Diff", "text/x-diff", "diff", "patch", "rej"),
            arrayOf("DTD", "application/xml-dtd"),
            arrayOf("ECL", "text/x-ecl", "ecl"),
            arrayOf("Eiffel", "text/x-eiffel", "e"),
            arrayOf("Erlang", "text/x-erlang", "erl", "hrl", "yaws"),
            arrayOf("Fortran", "text/x-Fortran", "f", "for", "f90", "f95"),
            arrayOf("Gas", "text/x-gas", "as", "gas"),
            arrayOf("Go", "text/x-go", "go"),
            arrayOf("Groovy", "text/x-groovy", "groovy", "gvy", "gy", "gsh"),
            arrayOf("HAML", "text/x-haml", "haml"),
            arrayOf("Haskell", "text/x-haskell", "hs"),
            arrayOf("ASP.net", "text/x-aspx", "asp", "aspx"),
            arrayOf("JSP", "text/x-jsp", "jsp"),
            arrayOf("HTML", "text/html", "html", "htm", "xhtml"),
            arrayOf("Jade", "text/x-jade", "jade"),
            arrayOf("JavaScript", "text/javascript", "js", "javascript"),
            arrayOf("Jinja2", "jinja2"),
            arrayOf("LiveScript", "text/x-livescript", "ls"),
            arrayOf("Lua", "text/x-lua", "lua"),
            arrayOf("Markdown", "text/x-markdown", "md", "markdown"),
            arrayOf("Markdown (Github)", "gfm", "md", "markdown"),
            arrayOf("Nginx", "text/nginx", "conf"),
            arrayOf("OCaml", "text/x-ocaml", "ocaml", "ml", "mli"),
            arrayOf("Matlab", "text/x-octave", "fig", "m", "mat"),
            arrayOf("Pascal", "text/x-pascal", "p", "pp", "pas"),
            arrayOf("PHP", "application/x-httpd-php", "php"),
            arrayOf("Pig Latin", "text/x-pig", "pig"),
            arrayOf("Perl", "text/x-perl", "pl"),
            arrayOf("Ini", "text/x-ini", "ini"),
            arrayOf("Properties", "text/x-properties", "properties"),
            arrayOf("Python", "text/x-python", "py"),
            arrayOf("R", "text/x-rsrc", "r"),
            arrayOf("Ruby", "text/x-ruby", "rb"),
            arrayOf("Rust", "text/x-rustsrc", "rs"),
            arrayOf("Scala", "text/x-scala", "scala"),
            arrayOf("Scheme", "text/x-scheme", "scm", "ss"),
            arrayOf("Shell", "text/x-sh", "sh", "bash"),
            arrayOf("Smalltalk", "text/x-stsrc", "st"),
            arrayOf("SQL", "text/x-sql", "sql"),
            arrayOf("SVG", "image/svg+xml", "svg"),
            arrayOf("TeX", "text/x-stex", "cls", "latex", "tex", "sty", "dtx", "ltx", "bbl"),
            arrayOf("VBScript", "text/vbscript", "vbs", "vbe", "wsc"),
            arrayOf("XML", "application/xml", "xml"),
            arrayOf("Kotlin", "text/x-kotlin", "kt", "kts"),
            arrayOf("YAML", "text/x-yaml", "yml", "yaml")
        )

    /**
     * Map to get the mimeType from any known file extension.
     */
    private val extensionToMimeType : HashMap<String, String>

    init {
        val result = HashMap<String, String>()
        for (language in FILENAME_EXTENSION_ARRAY) {
            for (j in 2 until language.size) {
                result[language[j]] = language[1]
            }
        }
        extensionToMimeType = result
    }

    /**
     * @return the mime type of the given fileName, if known
     */
    fun detectMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "")
        return extensionToMimeType[extension] ?: "text/plain"
    }
}