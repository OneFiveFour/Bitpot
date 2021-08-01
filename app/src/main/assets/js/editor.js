 (function() {
  var lang = void 0;

  var editor = void 0;

  var themeName = "midnight";

  var backgroundColor = "#0F192A";

  var lineNumbers = true;

  var lineWrapping = false;

  var displayFileContent = function() {

    var rawCodes = KotlinExecutor.getCode();

    $('#editor').text(rawCodes);
    var editorElm = document.getElementById("editor");
    var editorOption = {
      lineNumbers: lineNumbers,
      lineWrapping: lineWrapping,
      mode: lang,
      matchBrackets: true,
      readOnly: true,
      theme: themeName,
      scrollbarStyle: "null"
    };

    editor = CodeMirror.fromTextArea(editorElm, editorOption);
    $("body").css("background-color", backgroundColor);
  };

  window.setLang = function(codeLanguage) {
    lang = codeLanguage;
    if (editor) {
      return editor.setOption("mode", lang);
    }
  };

  window.setLineWrap = function(enabled) {
    lineWrapping = enabled
    if (editor) {
        return editor.setOption("lineWrapping", enabled)
    }
  }

  window.setLineNumbers = function(enabled) {
    lineNumbers = enabled
    if (editor) {
        return editor.setOption("lineNumbers", enabled)
    }
  }

  window.setDarkMode = function(enabled) {
    themeName = "default";
    backgroundColor = "white";
    if (enabled) {
        themeName = "midnight";
        backgroundColor = "#0F192A";
    }
    KotlinExecutor.onDarkModeChecked()
  }

  window.update = displayFileContent;

  $(document).ready(function() {
      KotlinExecutor.onWebViewReady();
  });

}).call(this);