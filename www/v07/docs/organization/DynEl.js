/*
 * File: DynEl.js
 * Include with: <SCRIPT SRC="DynEl.js"></SCRIPT>
 *
 * This file defines the DynEl class, which provides a portable API
 * to many Dynamic HTML features.
 */

/*
 * This is the constructor function for DynEl objects.
 * The arguments are the following:
 *   window: The Window object in which the dynamic element is to appear
 *   id:     The HTML ID for the dynamic element. Must be unique.
 *   body:   HTML text that constitutes the body of the dynamic element
 *   left:   The optional initial X-coordinate of the element
 *   top:    The optional initial Y-coordinate of the element
 *   width:  The optional width of the element
 * 
 * This constructor outputs a style sheet into the current document.
 * This means that it can only be called from the <HEAD> of the document
 * before any text has been output for display.
 */
function DynEl(window, id, body, left, top, width) {
    // Remember some arguments for later.
    this.window = window;
    this.id = id;
    this.body = body;

    // Output a CSS-P style sheet for this element.
    var d = window.document;
    d.writeln('<STYLE TYPE="text/css">');
    d.write('#' + id + ' {position:absolute;');
    if (left) d.write('left:' + left + ';');
    if (top) d.write('top:' + top + ';');
    if (width) d.write('width:' + width + ';');
    d.writeln('}');
    d.writeln('</STYLE>');
}

/*
 * Now we define a bunch of methods for the DynEl class.  
 * We define one set of methods if we are running in Navigator, and
 * another set of methods if we are running in Internet Explorer.
 * Note that the APIs of the methods are the same in both cases; it
 * is only the method bodies that change. In this way, we define
 * a portable API to the common DHTML functionality of the two browsers.
 */

// First, define the Navigator methods.
if (navigator.appName.indexOf("Netscape") != -1) {

    /*
     * This function outputs the dynamic element itself into the document.
     * It must be called before any other methods of the DynEl object can
     * be used.
     */  
    DynEl.prototype.output = function() {
        var d = this.window.document;  // Shortcut variable: saves typing

        // Output the element within a <DIV> tag.  Specify the element id.
        d.writeln('<DIV ID="' + this.id + '">');
        d.writeln(this.body);
        d.writeln("</DIV>");

        // Now, for convenience, save a reference to the Layer object
        // created by this dynamic element.
        this.layer = d[this.id];
    }

    // Here are methods for moving, hiding, stacking, and otherwise
    // manipulating the dynamic element.
    DynEl.prototype.moveTo = function(x,y) { this.layer.moveTo(x,y); }
    DynEl.prototype.moveBy = function(x,y) { this.layer.moveBy(x,y); }
    DynEl.prototype.show = function() { this.layer.visibility = "show"; }
    DynEl.prototype.hide = function() { this.layer.visibility = "hide"; }
    DynEl.prototype.setStackingOrder = function(z) { this.layer.zIndex = z; }
    DynEl.prototype.setBgColor = function(color) {
        this.layer.bgColor = color; 
    }
    DynEl.prototype.setBgImage = function(image) { 
        this.layer.background.src = image;
    }

    // These methods query the position, size, and other properties
    // of the dynamic element.
    DynEl.prototype.getX = function() { return this.layer.left; }
    DynEl.prototype.getY = function() { return this.layer.right; }
    DynEl.prototype.getWidth = function() { return this.layer.width; }
    DynEl.prototype.getHeight = function() { return this.layer.height; }
    DynEl.prototype.getStackingOrder = function() { return this.layer.zIndex; }
    DynEl.prototype.isVisible = function() { 
        return this.layer.visibility == "show"; 
    }

    /* 
     * This method allows us to dynamically change the contents of
     * the dynamic element. The argument or arguments should be HTML
     * strings which become the new body of the element.
     */
    DynEl.prototype.setBody = function() {
        for(var i = 0; i < arguments.length; i++)
            this.layer.document.writeln(arguments[i]);
        this.layer.document.close();
    }

    /*
     * This method registers a handler for the named event on the
     * element. The event name argument should be the name of an
     * event handler property, such as "onmousedown" or "onkeypress".
     * The handler is a function that takes whatever action is necessary.
     * Because Navigator and IE do not have compatible Event objects,
     * all event details are passed as arguments to the handler function.
     * When invoked, the handler will be passed the following nine arguments:
     *   1) A reference to the DynEl object
     *   2) A string containing the event type
     *   3) The X-coordinate of the mouse, relative to the DynEl
     *   4) The Y-coordinate of the mouse.
     *   5) The mouse button that was clicked (if any)
     *   6) The Unicode code of the key that was pressed (if any)
     *   7) A boolean specifying whether the Shift key was down
     *   8) A boolean specifying whether the Control key was down
     *   9) A boolean specifying whether the Alt key was down
     * Event handlers that are not interested in all these arguments do
     * not have to declare them all in their argument lists, of course.
     */
    DynEl.prototype.addEventHandler = function(eventname, handler) {
        // Arrange to capture events on this layer.
        this.layer.captureEvents(DynEl._eventmasks[eventname]);
        var dynel = this;  // Current DynEl for use in the nested function.
        // Define an event handler that will invoke the specified handler,
        // and pass it the nine arguments specified above.
        this.layer[eventname] = function(event) { 
            return handler(dynel, event.type, event.x, event.y, 
                           event.which, event.which,
                           ((event.modifiers & Event.SHIFT_MASK) != 0),
                           ((event.modifiers & Event.CTRL_MASK) != 0),
                           ((event.modifiers & Event.ALT_MASK) != 0));
        }
    }

    /*
     * This method unregisters the named event handler. It should be 
     * called with a single string argument such as "onmouseover".
     */
    DynEl.prototype.removeEventHandler = function(eventname) {
        this.layer.releaseEvents(DynEl._eventmasks[eventname]);
        delete this.layer[eventname];
    }

    /*
     * This array is used internally by the two methods above to map
     * from event name to event type.
     */
    DynEl._eventmasks = {
      onabort:Event.ABORT, onblur:Event.BLUR, onchange:Event.CHANGE,
      onclick:Event.CLICK, ondblclick:Event.DBLCLICK, 
      ondragdrop:Event.DRAGDROP, onerror:Event.ERROR, 
      onfocus:Event.FOCUS, onkeydown:Event.KEYDOWN,
      onkeypress:Event.KEYPRESS, onkeyup:Event.KEYUP, onload:Event.LOAD,
      onmousedown:Event.MOUSEDOWN, onmousemove:Event.MOUSEMOVE, 
      onmouseout:Event.MOUSEOUT, onmouseover:Event.MOUSEOVER, 
      onmouseup:Event.MOUSEUP, onmove:Event.MOVE, onreset:Event.RESET,
      onresize:Event.RESIZE, onselect:Event.SELECT, onsubmit:Event.SUBMIT,
      onunload:Event.UNLOAD
    };
}

/*
 * Now define methods for Internet Explorer.
 * These methods have identical APIs to the ones defined for Netscape
 * above. Therefore, we will not repeat all the comments above.
 */
if (navigator.appName.indexOf("Microsoft") != -1) {

    // The all-important output() method
    DynEl.prototype.output = function() {
        var d = this.window.document;  // Shortcut variable: saves typing

        // Output the element within a <DIV> tag.  Specify the element id.
        d.writeln('<DIV ID="' + this.id + '">');
        d.writeln(this.body);
        d.writeln("</DIV>");

        // Now, for convenience, save references to the <DIV> element
        // we've created, and to its associated Style element.
        // These will be used throughout the methods that follow.
        this.element = d.all[this.id];
        this.style = this.element.style;
    }

    // Methods to move the dynamic object
    DynEl.prototype.moveTo = function(x,y) {
        this.style.pixelLeft = x;
        this.style.pixelTop = y;
    }
    DynEl.prototype.moveBy = function(x,y) {
        this.style.pixelLeft += x;
        this.style.pixelTop += y;
    }

    // Methods to set other attributes of the dynamic object
    DynEl.prototype.show = function() { this.style.visibility = "visible"; }
    DynEl.prototype.hide = function() { this.style.visibility = "hidden"; }
    DynEl.prototype.setStackingOrder = function(z) { this.style.zIndex = z; }
    DynEl.prototype.setBgColor = function(color) { 
        this.style.backgroundColor = color; 
    }
    DynEl.prototype.setBgImage = function(image) { 
        this.style.backgroundImage = image;
    }

    // Methods to query the dynamic object
    DynEl.prototype.getX = function() { return this.style.pixelLeft; }
    DynEl.prototype.getY = function() { return this.style.pixelRight; }
    DynEl.prototype.getWidth = function() { return this.style.width; }
    DynEl.prototype.getHeight = function() { return this.style.height; }
    DynEl.prototype.getStackingOrder = function() { return this.style.zIndex; }
    DynEl.prototype.isVisible = function() { 
        return this.style.visibility == "visible"; 
    }

    // Change the contents of the dynamic element.
    DynEl.prototype.setBody = function() {
        var body = "";
        for(var i = 0; i < arguments.length; i++) {
            body += arguments[i] + "\n";
        }
        this.element.innerHTML = body;
    }

    // Define an event handler.
    DynEl.prototype.addEventHandler = function(eventname, handler) {
        var dynel = this;  // Current DynEl for use in the nested function
        // Set an IE4 event handler that invokes the specified handler
        // with the appropriate nine arguments.
        this.element[eventname] = function() { 
            var e = dynel.window.event;
            e.cancelBubble = true;
            return handler(dynel, e.type, e.x, e.y, 
                           e.button, e.keyCode, 
                           e.shiftKey, e.ctrlKey, e.altKey); 
        }
    }

    // Remove an event handler.
    DynEl.prototype.removeEventHandler = function(eventname) {
        delete this.element[eventname];
    }
}
