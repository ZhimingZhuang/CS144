
function StateSuggestions() {
    
}

StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl,
                                                          bTypeAhead) {
    var xmlHttp = new XMLHttpRequest();
    var control = oAutoSuggestControl;
    var sTextboxValue = oAutoSuggestControl.textbox.value;

    if(sTextboxValue.length == 0) {
        var aSuggestions = [];
        control.autosuggest(aSuggestions, bTypeAhead);
        return;
    }
    else {
        xmlHttp.onreadystatechange = function () {
            if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                var s = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');
                var aSuggestions = [];

                if(sTextboxValue.length > 0) {
                    for (var i = 0; i < s.length; i++) {
                        aSuggestions.push(s[i].childNodes[0].getAttribute("data"));
                    }
                }

                control.autosuggest(aSuggestions, bTypeAhead);
            }
        }

        xmlHttp.open("GET", "suggest?input=" + encodeURIComponent(sTextboxValue), true);
        xmlHttp.send();
    }

};