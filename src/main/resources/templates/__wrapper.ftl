<#ftl strip_whitespace=true>

<#macro main subtitle="-">
    <#setting number_format="0.#######">
    <#setting url_escaping_charset='UTF-8'>

<!doctype html>
<html>
<head>
    <title>${subtitle} - QND</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.3.5/dist/css/bootstrap.min.css">
</head>
<body>

    <#nested/>

</body>
</html>
</#macro>
