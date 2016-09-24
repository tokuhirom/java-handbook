<#ftl strip_whitespace=true>

<#--
 * message
 *
 * Macro to translate a message code into a message.
 -->
<#macro message code>${springMacroRequestContext.getMessage(code)}</#macro>

<#--
 * messageText
 *
 * Macro to translate a message code into a message,
 * using the given default text if no message found.
 -->
<#macro messageText code, text>${springMacroRequestContext.getMessage(code, text)}</#macro>

<#--
 * messageArgs
 *
 * Macro to translate a message code with arguments into a message.
 -->
<#macro messageArgs code, args>${springMacroRequestContext.getMessage(code, args)}</#macro>
