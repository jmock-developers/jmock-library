
var testFrameworks = ["JUnit4", "JUnit3", "Raw"];


function setClassOnAll(elements, newClass) {
  for (var i = 0; i < elements.length; i++) {
    elements[i].className = newClass;
  }
}

function selectTestFramework(frameworkToShow) {
  for (var i = 0; i < testFrameworks.length; i++) {
    var framework = testFrameworks[i];
    var newClass;
    
    if (framework == frameworkToShow) {
      newClass = framework + " Selected";
    }
    else {
      newClass = framework + " Deselected";
    }
    
    setClassOnAll(elementsWithClass(framework), newClass);
  }
  
  setCookie("preferredTestFramework", frameworkToShow);
}

function restorePreferredTestFramework() {
  framework = getCookie("preferredTestFramework");
  if (framework == null) framework = "Raw";
  selectTestFramework(framework);
}

function elementsWithClass(searchClass, node, tag) {
  var classElements = new Array();
  
  if ( node == null ) node = document;
  if ( tag == null ) tag = '*';
  
  var els = node.getElementsByTagName(tag);
  var pattern = new RegExp('(^|\\s)'+searchClass+'(\\s|$)');
  
  for (i = 0, j = 0; i < els.length; i++) {
    if (pattern.test(els[i].className)) {
      classElements[j] = els[i];
      j++;
    }
  }
  
  return classElements;
}

function setCookie(name, value) {
  now = new Date();
  expiry = now.setYear(now.getYear()+1);
  
  document.cookie = name + "=" + value + "; Max-Age=40000000";
}

function getCookie(name) {
  var cookies = document.cookie;
  var prefix = name + "=";
  var begin = cookies.indexOf("; " + prefix);
  
  if (begin == -1) {
    begin = cookies.indexOf(prefix);
    if (begin != 0) return null;
  }
  else {
    begin += 2;
  }
  
  var end = cookies.indexOf(";", begin);
  if (end == -1) {
    end = cookies.length;
  }
  
  return unescape(cookies.substring(begin + prefix.length, end));
}


