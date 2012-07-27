/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

/**
 *
 * @author tobias
 */
public class HttpBrowserCapabilities {
	public boolean getActiveXControls() {
		//Gets a value indicating whether the browser supports ActiveX controls. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public property 	Adapters 	
					//Infrastructure. Returns the collection of available control adapters. (Inherited from HttpCapabilitiesBase.)

	public property 	AOL 	
					//Gets a value indicating whether the client is an America Online (AOL) browser. (Inherited from HttpCapabilitiesBase.)
	public property 	BackgroundSounds 	
					//Gets a value indicating whether the browser supports playing background sounds using the <bgsounds> HTML element. (Inherited from HttpCapabilitiesBase.)
	public property 	Beta 	
					//Gets a value indicating whether the browser is a beta version. (Inherited from HttpCapabilitiesBase.)
	public property 	Browser 	
					//Gets the browser string (if any) that was sent by the browser in the User-Agent request header. (Inherited from HttpCapabilitiesBase.)
	public property 	Browsers 	
					//Gets an ArrayList of the browsers in the Capabilities dictionary. (Inherited from HttpCapabilitiesBase.)
	
  public boolean canCombineFormsInDeck() {
    //Gets a value indicating whether the browser supports decks that contain multiple forms, such as separate cards. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canInitiateVoiceCall() {
    //Gets a value indicating whether the browser device is capable of initiating a voice call. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canRenderAfterInputOrSelectElement() {
    //Gets a value indicating whether the browser supports page content following WML <select> or <input> elements. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canRenderEmptySelects() {
    //Gets a value indicating whether the browser supports empty HTML <select> elements. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canRenderInputAndSelectElementsTogether() {
    //Gets a value indicating whether the browser supports WML INPUT and SELECT elements together on the same card. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canRenderMixedSelects() {
    //Gets a value indicating whether the browser supports WML <option> elements that specify both onpick and value attributes. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canRenderOneventAndPrevElementsTogether() {
    //Gets a value indicating whether the browser supports WML <onevent> and <prev> elements that coexist within the same WML card. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canRenderPostBackCards() {
    //Gets a value indicating whether the browser supports WML cards for postback. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public boolean canRenderSetvarZeroWithMultiSelectionList() {
    //Gets a value indicating whether the browser supports WML <setvar> elements with a value attribute of 0. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
	
  public boolean canSendMail() {
    //Gets a value indicating whether the browser supports sending e-mail by using the HTML <mailto> element for displaying electronic addresses. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public property 	Capabilities 	
					//Infrastructure. Used internally to get the defined capabilities of the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	CDF 	
					//Gets a value indicating whether the browser supports Channel Definition Format (CDF) for webcasting. (Inherited from HttpCapabilitiesBase.)
	public property 	ClrVersion 	
					//Gets the version of the .NET Framework that is installed on the client. (Inherited from HttpCapabilitiesBase.)
	public property 	Cookies 	
					//Gets a value indicating whether the browser supports cookies. (Inherited from HttpCapabilitiesBase.)
	public property 	Crawler 	
					//Gets a value indicating whether the browser is a search engine Web crawler. (Inherited from HttpCapabilitiesBase.)
	public property 	DefaultSubmitButtonLimit 	
					//Returns the maximum number of Submit buttons that are allowed for a form. (Inherited from HttpCapabilitiesBase.)
	public property 	EcmaScriptVersion 	
					//Gets the version number of ECMAScript that the browser supports. (Inherited from HttpCapabilitiesBase.)
	public property 	Frames 	
					//Gets a value indicating whether the browser supports HTML frames. (Inherited from HttpCapabilitiesBase.)
	public property 	GatewayMajorVersion 	
					//Gets the major version number of the wireless gateway used to access the server, if known. (Inherited from HttpCapabilitiesBase.)
	public property 	GatewayMinorVersion 	
					//Gets the minor version number of the wireless gateway used to access the server, if known. (Inherited from HttpCapabilitiesBase.)
	public property 	GatewayVersion 	
					//Gets the version of the wireless gateway used to access the server, if known. (Inherited from HttpCapabilitiesBase.)
	public boolean getHasBackButton() {
		//Gets a value indicating whether the browser has a dedicated Back button. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public property 	HidesRightAlignedMultiselectScrollbars() {
		//Gets a value indicating whether the scrollbar of an HTML <select multiple> element with an align attribute value of right is obscured upon rendering. (Inherited from HttpCapabilitiesBase.)
	}
	
	public property 	HtmlTextWriter() {
					//Gets or sets the fully qualified class name of the HtmlTextWriter to use. (Inherited from HttpCapabilitiesBase.)
	}
	
	public property 	Id 	
    //Gets the internal identifier of the browser as specified in the browser definition file. (Inherited from HttpCapabilitiesBase.)
          
	public property 	getInputType() {
    //Returns the type of input supported by browser. (Inherited from HttpCapabilitiesBase.)
  }
	
  public boolean isColor() {
		//Gets a value indicating whether the browser has a color display. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
  
	public property 	IsMobileDevice 	
					//Gets a value indicating whether the browser is a recognized mobile device. (Inherited from HttpCapabilitiesBase.)
	public property 	Item 	
					//Gets the value of the specified browser capability. In C#, this property is the indexer for the class. (Inherited from HttpCapabilitiesBase.)
	public property 	JavaApplets 	
					//Gets a value indicating whether the browser supports Java. (Inherited from HttpCapabilitiesBase.)
	public property 	JavaScript 	
					//Obsolete. Gets a value indicating whether the browser supports JavaScript. (Inherited from HttpCapabilitiesBase.)
	public property 	JScriptVersion 	
					//Gets the Jscript version that the browser supports. (Inherited from HttpCapabilitiesBase.)
	public property 	MajorVersion 	
					//Gets the major (integer) version number of the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	MaximumHrefLength 	
					//Gets the maximum length in characters for the href attribute of an HTML <a> (anchor) element. (Inherited from HttpCapabilitiesBase.)
	public property 	MaximumRenderedPageSize 	
					//Gets the maximum length of the page, in bytes, which the browser can display. (Inherited from HttpCapabilitiesBase.)
	public property 	MaximumSoftkeyLabelLength 	
					//Returns the maximum length of the text that a soft-key label can display. (Inherited from HttpCapabilitiesBase.)
	public property 	MinorVersion 	
					//Gets the minor (that is, decimal) version number of the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	MinorVersionString 	
					//Gets the minor (decimal) version number of the browser as a string. (Inherited from HttpCapabilitiesBase.)
	public property 	MobileDeviceManufacturer 	
					//Returns the name of the manufacturer of a mobile device, if known. (Inherited from HttpCapabilitiesBase.)
	public property 	MobileDeviceModel 	
					//Gets the model name of a mobile device, if known. (Inherited from HttpCapabilitiesBase.)
	public property 	MSDomVersion 	
					//Gets the version of Microsoft HTML (MSHTML) Document Object Model (DOM) that the browser supports. (Inherited from HttpCapabilitiesBase.)
	public property 	NumberOfSoftkeys 	
					//Returns the number of soft keys on a mobile device. (Inherited from HttpCapabilitiesBase.)
	public property 	Platform 	
					//Gets the name of the platform that the client uses, if it is known. (Inherited from HttpCapabilitiesBase.)
	public property 	PreferredImageMime 	
					//Returns the MIME type of the type of image content typically preferred by the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	PreferredRenderingMime 	
					//Returns the MIME type of the type of content typically preferred by the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	PreferredRenderingType 	
					//Gets the general name for the type of content that the browser prefers. (Inherited from HttpCapabilitiesBase.)
	public property 	PreferredRequestEncoding 	
					//Gets the request encoding preferred by the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	PreferredResponseEncoding 	
					//Gets the response encoding preferred by the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	RendersBreakBeforeWmlSelectAndInput 	
					//Gets a value indicating whether the browser renders a line break before <select> or <input> elements. (Inherited from HttpCapabilitiesBase.)
	public property 	RendersBreaksAfterHtmlLists 	
					//Gets a value indicating whether the browser renders a line break after list-item elements. (Inherited from HttpCapabilitiesBase.)
	public property 	RendersBreaksAfterWmlAnchor 	
					//Gets a value indicating whether the browser renders a line break after a stand-alone HTML <a> (anchor) element. (Inherited from HttpCapabilitiesBase.)
	public property 	RendersBreaksAfterWmlInput 	
					//Gets a value indicating whether the browser renders a line break after an HTML <input> element. (Inherited from HttpCapabilitiesBase.)
	public property 	RendersWmlDoAcceptsInline 	
					//Gets a value indicating whether the mobile-device browser renders a WML do-based form accept construct as an inline button rather than as a soft key. (Inherited from HttpCapabilitiesBase.)
	public property 	RendersWmlSelectsAsMenuCards 	
					//Gets a value indicating whether the browser renders WML <select> elements as menu cards, rather than as a combo box. (Inherited from HttpCapabilitiesBase.)
	public property 	RequiredMetaTagNameValue 	
					//Infrastructure. Used internally to produce a meta-tag required by some browsers. (Inherited from HttpCapabilitiesBase.)
	
  public boolean requiresAttributeColonSubstitution() {
    //Gets a value indicating whether the browser requires colons in element attribute values to be substituted with a different character. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
    
	public boolean requiresContentTypeMetaTag() {
    //Gets a value indicating whether the browser requires an HTML <meta> element for which the content-type attribute is specified. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
    
	public boolean requiresControlStateInSession() {
    //Gets a value indicating whether the browser requires control state to be maintained in sessions. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
    
	public boolean requiresDBCSCharacter() {
    //Gets a value indicating whether the browser requires a double-byte character set. (Inherited from HttpCapabilitiesBase.)
    return false;
  }

  public boolean requiresHtmlAdaptiveErrorReporting() {
    //Gets a value indicating whether the browser requires nonstandard error messages. (Inherited from HttpCapabilitiesBase.)
    return false;
  }

	public boolean requiresLeadingPageBreak() {
    //Gets a value indicating whether the browser requires the first element in the body of a Web page to be an HTML <br> element. (Inherited from HttpCapabilitiesBase.)
    return false;
  }

	public boolean requiresNoBreakInFormatting() {
    //Gets a value indicating whether the browser does not support HTML <br> elements to format line breaks. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
    
	public boolean requiresOutputOptimization() {
    //Gets a value indicating whether the browser requires pages to contain a size-optimized form of markup language tags. (Inherited from HttpCapabilitiesBase.)
        return false;
  }

	public boolean requiresPhoneNumbersAsPlainText() {
    //Gets a value indicating whether the browser supports phone dialing based on plain text, or whether it requires special markup. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
    
	public boolean requiresSpecialViewStateEncoding() {
    //Gets a value indicating whether the browser requires VIEWSTATE values to be specially encoded. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
    
	public boolean requiresUniqueFilePathSuffix() {
    //Gets a value indicating whether the browser requires unique form-action URLs. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
    
	public boolean requiresUniqueHtmlCheckboxNames() {
    //Gets a value indicating whether the browser requires unique name attribute values of multiple HTML <input type="checkbox"> elements. (Inherited from HttpCapabilitiesBase.)
    return false;
  }

  public boolean requiresUniqueHtmlInputNames() {
    //Gets a value indicating whether the browser requires unique name attribute values of multiple HTML <input> elements. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
	
  public boolean requiresUrlEncodedPostfieldValues() {
		//Gets a value indicating whether postback data sent by the browser will be UrlEncoded. (Inherited from HttpCapabilitiesBase.)
    return false;
  }
	
  public property 	ScreenBitDepth 	
					//Returns the depth of the display, in bits per pixel. (Inherited from HttpCapabilitiesBase.)
	public property 	ScreenCharactersHeight 	
					//Returns the approximate height of the display, in character lines. (Inherited from HttpCapabilitiesBase.)
	public property 	ScreenCharactersWidth 	
					//Returns the approximate width of the display, in characters. (Inherited from HttpCapabilitiesBase.)
	public property 	ScreenPixelsHeight 	
					//Returns the approximate height of the display, in pixels. (Inherited from HttpCapabilitiesBase.)
	public property 	ScreenPixelsWidth 	
					//Returns the approximate width of the display, in pixels. (Inherited from HttpCapabilitiesBase.)
	
	public boolean 	supportsAccesskeyAttribute() {
		//Gets a value indicating whether the browser supports the ACCESSKEY attribute of HTML <a> (anchor) and <input> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public boolean 	supportsBodyColor() {
		//Gets a value indicating whether the browser supports the bgcolor attribute of the HTML <body> element. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsBold() {
		//Gets a value indicating whether the browser supports HTML <b> elements to format bold text. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsCacheControlMetaTag() {
		//Gets a value indicating whether the browser supports the cache-control value for the http-equiv attribute of HTML <meta> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsCallback() {
		//Gets a value indicating whether the browser supports callback scripts. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsCss() {
		//Gets a value indicating whether the browser supports Cascading Style Sheets (CSS). (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsDivAlign() {
		//Gets a value indicating whether the browser supports the align attribute of HTML <div> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsDivNoWrap() {
		//Gets a value indicating whether the browser supports the nowrap attribute of HTML <div> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsEmptyStringInCookieValue() {
		//Gets a value indicating whether the browser supports empty (null) strings in cookie values. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsFontColor() {
		//Gets a value indicating whether the browser supports the color attribute of HTML <font> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsFontName() {
		//Gets a value indicating whether the browser supports the name attribute of HTML <font> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsFontSize() {
		//Gets a value indicating whether the browser supports the size attribute of HTML <font> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsImageSubmit() {
		//Gets a value indicating whether the browser supports using a custom image in place of a standard form Submit button. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsIModeSymbols() {
		//Gets a value indicating whether the browser supports i-mode symbols. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsInputIStyle() {
		//Gets a value indicating whether the browser supports the istyle attribute of HTML <input> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsInputMode() {
		//Gets a value indicating whether the browser supports the mode attribute of HTML <input> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public boolean 	supportsItalic() {
		//Gets a value indicating whether the browser supports HTML <i> elements to format italic text. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsJPhoneMultiMediaAttributes() {
		//Gets a value indicating whether the browser supports J-Phone multimedia attributes. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsJPhoneSymbols() {
		//Gets a value indicating whether the browser supports J-Phone–specific picture symbols. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsQueryStringInFormAction() {
		//Gets a value indicating whether the browser supports a query string in the action attribute value of HTML <form> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsRedirectWithCookie() {
		//Gets a value indicating whether the browser supports cookies on redirection. (Inherited from HttpCapabilitiesBase.)
		return false;
	}

	public boolean 	supportsSelectMultiple() {
		//Gets a value indicating whether the browser supports the multiple attribute of HTML <select> elements. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public boolean supportsUncheck() {
		//Gets a value indicating whether the clearing of a checked HTML <input type=checkbox> element is reflected in postback data. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public boolean supportsXmlHttp() {
		//Gets a value indicating whether the browser supports receiving XML over HTTP. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public property 	Tables 	
					//Gets a value indicating whether the browser supports HTML <table> elements. (Inherited from HttpCapabilitiesBase.)
	public property 	TagWriter 	
					//Infrastructure. Used internally to get the type of the object that is used to write tags for the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	Type 	
					//Gets the name and major (integer) version number of the browser. (Inherited from HttpCapabilitiesBase.)
	public property 	UseOptimizedCacheKey 	
					//Used internally to get a value indicating whether to use an optimized cache key. (Inherited from HttpCapabilitiesBase.)
	public property 	VBScript 	
					//Gets a value indicating whether the browser supports Visual Basic Scripting edition (VBScript). (Inherited from HttpCapabilitiesBase.)
	public property 	Version 	
					//Gets the full version number (integer and decimal) of the browser as a string. (Inherited from HttpCapabilitiesBase.)
	public property 	W3CDomVersion 	
					//Gets the version of the World Wide Web Consortium (W3C) XML Document Object Model (DOM) that the browser supports. (Inherited from HttpCapabilitiesBase.)
	
	public boolean isWin16() {
		//Gets a value indicating whether the client is a Win16-based computer. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
	
	public boolean isWin32() { 	
		//Gets a value indicating whether the client is a Win32-based computer. (Inherited from HttpCapabilitiesBase.)
		return false;
	}
}
