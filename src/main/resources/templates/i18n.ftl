<#import "/__wrapper.ftl" as wrapper>
<@wrapper.main>

<p>
    <@spring.messageArgs "greeting.message", ['John'] />
</p>
<hr>
    <@spring.message "intro" />

<hr>

Current locale: ${springMacroRequestContext.locale}

<hr>

<form method="post" action="/set-locale">
    <input type="submit" name="locale" value="ko"/>
</form>

<form method="post" action="/set-locale">
    <input type="submit" name="locale" value="ja"/>
</form>

<form method="post" action="/set-locale">
    <input type="submit" name="locale" value="en"/>
</form>

<form method="post" action="/set-locale">
    <input type="submit" name="locale" value="de"/>
</form>

</@wrapper.main>
