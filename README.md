# scala-xpath
Scala Xpath

This is XPath generator that can create Xpath for given attribute name and value.

Example:

For given XML document:

```
  val html: Elem =
    <html>
      <head>
        <title>This is something</title>
      </head>
      <body>
        Welcome on my blog!
        <br/>
        <div id="authentication">
          Login form:
          <form action="login">
            Login:
            <input name="login"/>
            <br/>
            Password:
            <input name="password" type="password"/>
            <br/>
            <input type="submit"/>
          </form>
        </div>
        <br/>
        <div class="first">
          <div class="apply-external">
            <div>Similiar jobs</div>
            <div>Pleas fill the form</div>
            <div>
              <form action="/yourname">
                Name:
                <input type="name"/>
                <br/>
                <input type="submit"/>
              </form>
            </div>
            <br/>
          </div>
        </div>
      </body>
    </html>
```

If we want to find an Xpath for element that contains an attribute "action" with value "/yourname" we can use following method:

```
  val xpath: String = XpathGenerator.createXPathForAttribute(html, "action", "/yourname")
```

The result will be:

//div[@class='first']/div[@class='apply-external']/div[3]/form[1]/@action


