
	function crearICS() {
		var icsMSG = "BEGIN:VCALENDAR\n";
		icsMSG += "VERSION:2.0\n";
		icsMSG += "PRODID:" + myJSONCal.PRODID + "n";
		icsMSG += "X-WR-CALNAME:" + myJSONCal.CALNAME + "\n";
		icsMSG += "CALSCALE=GREGORIAN\n";
		for (i = 0; i < myJSONCal.EVENTS.length; i++) { 
			icsMSG += "BEGIN:VEVENT\n";
			icsMSG += "DTSTAMP:" + myJSONCal.EVENTS[i].DTSTAMP + "\n";
			icsMSG += "UID:" + myJSONCal.EVENTS[i].UID + "\n";
			icsMSG += "DTSTART;TZID=" + myJSONCal.EVENTS[i].DTSTART +"\n";
			icsMSG += "DTEND;TZID:" + myJSONCal.EVENTS[i].DTEND +"\n";
			icsMSG += "SUMMARY:" + myJSONCal.EVENTS[i].SUMMARY + "\n";
			icsMSG += "DESCRIPTION:" + myJSONCal.EVENTS[i].DESCRIPTION + "\n";
			icsMSG += "LOCATION:" + myJSONCal.EVENTS[i].LOCATION + "\n";
			icsMSG += "END:VEVENT\n";
		}
		icsMSG += "END:VCALENDAR";
		window.open( "data:text/calendar;charset=utf8," + escape(icsMSG));
	}
	
	function crearICSPorEvento(indice) {
		var icsMSG = "BEGIN:VCALENDAR\n";
		icsMSG += "VERSION:2.0\n";
		icsMSG += "PRODID:" + myJSONCal.PRODID + "n";
		icsMSG += "X-WR-CALNAME:" + myJSONCal.CALNAME + "\n";
		icsMSG += "CALSCALE=GREGORIAN\n";
		if (myJSONCal.EVENTS.length > 0) {
			var ids = indice.split(",");
			for (i = 0; i < ids.length; i++) {
				var x = parseInt(ids[i]);
				icsMSG += "BEGIN:VEVENT\n";
				icsMSG += "DTSTAMP:" + myJSONCal.EVENTS[x].DTSTAMP + "\n";
				icsMSG += "UID:" + myJSONCal.EVENTS[x].UID + "\n";
				icsMSG += "DTSTART;TZID=" + myJSONCal.EVENTS[x].DTSTART +"\n";
				icsMSG += "DTEND;TZID:" + myJSONCal.EVENTS[x].DTEND +"\n";
				icsMSG += "SUMMARY:" + myJSONCal.EVENTS[x].SUMMARY + "\n";
				icsMSG += "DESCRIPTION:" + myJSONCal.EVENTS[x].DESCRIPTION + "\n";
				icsMSG += "LOCATION:" + myJSONCal.EVENTS[x].LOCATION + "\n";
				icsMSG += "END:VEVENT\n";
			}
		}
		icsMSG += "END:VCALENDAR";
		window.open( "data:text/calendar;charset=utf8," + escape(icsMSG));
	}