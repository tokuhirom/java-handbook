<#import "/__wrapper.ftl" as wrapper>
<@wrapper.main>

↓自動エスケープがきいているよという印
${xmp}

<h1>制御構文</h1>

<h2>loop</h2>

<pre>
    <#list ["foo", "bar", "baz"] as x>
    ${x}
    </#list>
</pre>

<h2>if</h2>
<pre>
    <#assign user="Big Joe">
    <#if user == "Big Joe">
        It is Big Joe
    </#if>
</pre>

<h2>include</h2>

<pre>
    <#include "/parts.ftl">
</pre>

<h2>switch</h2>

<pre>
    <#assign size="small">
    <#switch size>
        <#case "small">
            This will be processed if it is small
            <#break>
        <#case "medium">
            This will be processed if it is medium
            <#break>
        <#case "large">
            This will be processed if it is large
            <#break>
        <#default>
            This will be processed if it is neither
    </#switch>
</pre>

<h1>文字列系の処理</h1>

<h2>HTML自動エスケープモードのときに、自動エスケープ対象外にする。</h2>
<pre>${"<b>hello</b>"?no_esc}</pre>

<h2>文字列置換</h2>
<pre>${"this is a car acarus"?replace("car", "bulldozer")}</pre>

<h2>前方一致</h2>
<pre>${"redirect"?starts_with("red")?c}</pre>

<h2>文字列を含む？</h2>
<pre><#if "piceous"?contains("ice")>It contains "ice"</#if></pre>

<h2>後方一致</h2>
<pre><#if "piceous"?ends_with("ice")>It ends with "ice"</#if></pre>

<h2>文字列スライス</h2>
<pre>
${"hoge"[0]}
${"hoge"[1..]}
${"hoge"[1..2]}
${"hoge"[1..*2]} ←　str[from..*maxLength]
</pre>

<h2>大文字に</h2>
<pre>${"GrEeN MoUsE"?upper_case}</pre>

<h2>Capitalize</h2>
<pre>${"GreEN mouse"?capitalize}</pre>

<h2>URL escape:</h2>
<pre>${'a/b c'?url}</pre>

<h2>文字列の長さ</h2>
<pre>${"GreEN mouse"?length}</pre>

<h1>数字系の処理</h1>

<h2>絶対値</h2>
<pre>
${-5?abs}
${5?abs}
    </pre>

<h2>整数化</h2>
<pre>
    <#assign testlist=[
    0, 1, -1, 0.5, 1.5, -0.5,
    -1.5, 0.25, -0.25, 1.75, -1.75]>
    <#list testlist as result>
    ${result} ?floor=${result?floor} ?ceiling=${result?ceiling} ?round=${result?round}
    </#list>
</pre>

<h2>フォーマット</h2>
<pre>${3.14?string["0.#"]}</pre>

<h1>日時系の処理</h1>

<h2>現在の日時を得る</h2>
<pre>${.now?string}</pre>

<h2>SimpleDateFormat のパターンでフォーマットする</h2>
<pre>
${.now?string["yyyy-MM-dd(EEE) HH:mm"]}
</pre>

<h1>boolean 系の処理</h1>

<h2>文字列化</h2>
<pre>${true?c}</pre>

<h2>分岐しての文字列化</h2>
<pre>${true?string("yes", "no")}</pre>

<h1>リスト系の処理</h1>

<h2>join</h2>
<pre>${["red", "green", "blue"]?join(", ")}</pre>

<h2>reverse</h2>
<pre>${["red", "green", "blue"]?reverse?join(", ")}</pre>

<h2>size</h2>
<pre>${["red", "green", "blue"]?size}</pre>

<h2>sort</h2>
<pre>${["red", "green", "blue"]?sort?join(",")}</pre>

<h1>Map 系の処理</h1>

<h2>get</h2>
<pre>${{ "name": "mouse", "price": 50 }['name']}</pre>

<h2>keys</h2>
<pre>${{ "name": "mouse", "price": 50 }?keys?join(", ")}</pre>

<h2>values</h2>
<pre>${{ "name": "mouse", "price": 50 }?values?join(", ")}</pre>

<h2>ループ内変数</h2>

<pre>
    <#list ['a', 'b', 'c'] as x>
    ${x?index}
    ${x?has_next?c}
    ${x?is_even_item?c}
    ${x?is_first?c}
    ${x?is_last?c}
    ${x?is_odd_item?c}
        ---
    </#list>
</pre>

</@wrapper.main>
