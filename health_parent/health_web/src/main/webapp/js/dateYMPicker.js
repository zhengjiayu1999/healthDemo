(function($){
	
	
	
	var DateConfig = {
		monthsShort : ["1月", "2月", "3月", "4月", "5月", "6月", "6月", "8月", "9月", "10月", "11月", "12月"],
		parseFormat: function(format){
			var separator = format.match(/[.\/\-\s].*?/),
				parts = format.split(/\W+/);
			if (!separator || !parts || parts.length === 0){
				throw new Error("Invalid date format.");
			}
			return {separator: separator, parts: parts};
		},
		parseDate: function(date, format) {
			var parts = date.split(format.separator),date = new Date(),val;
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			date.setMilliseconds(0);
			if (parts.length === format.parts.length) {
				var year = date.getFullYear(), day = date.getDate(), month = date.getMonth();
				for (var i=0, cnt = format.parts.length; i < cnt; i++) {
					val = parseInt(parts[i], 10)||1;
					switch(format.parts[i]) {
						case 'dd':
						case 'd':
							day = val;
							date.setDate(val);
							break;
						case 'mm':
						case 'm':
							month = val - 1;
							date.setMonth(val - 1);
							break;
						case 'yy':
							year = 2000 + val;
							date.setFullYear(2000 + val);
							break;
						case 'yyyy':
							year = val;
							date.setFullYear(val);
							break;
					}
				}
				date = new Date(year, month, day, 0 ,0 ,0);
			}
			return date;
		},
		formatDate: function(date, format){
			var val = {
				d: date.getDate(),
				m: date.getMonth() + 1,
				yy: date.getFullYear().toString().substring(2),
				yyyy: date.getFullYear()
			};
			val.dd = (val.d < 10 ? '0' : '') + val.d;
			val.mm = (val.m < 10 ? '0' : '') + val.m;
			var date = [];
			for (var i=0, cnt = format.parts.length; i < cnt; i++) {
				date.push(val[format.parts[i]]);
			}
			return date.join(format.separator);
		},
		dateTemplete: '<div class="dataYMPicker">'+
				'<div class="dataYMPickerBody">'+
					'<div class="dateYMPicker leftPanel">'+
						'<div class="monthContainer"></div>'+
						'<div class="yearContainers">'+
							'<div class="yearTools"><span class="prev"> &#60; </span><span class="next">&#62; </span></div><div class="yearLists"></div>'+
						'</div>'+
					'</div>'+
					'<div class="dateYMPicker rightPanel">'+
						'<div class="monthContainer"></div>'+
						'<div class="yearContainers">'+
							'<div class="yearTools"><span class="prev"> &#60; </span><span class="next">&#62; </span></div><div class="yearLists"></div>'+
						 '</div>'+
					'</div>'+
				'</div>'+
				'<div class="dateYMFooter">'+
					'<div class="footContainer">'+
						 '<span class="footLabels">已选择：<label class="startEndPickerDate">2061-01 - 2061-02</label></span>'+
						 '<span class="foorBtns"><button class="buttons cancelBtn">cancel</button><button class="buttons applyBtn">apply</button></span>'+
					'</div>'+
				'</div>'+
		'</div>'	
	}
	
		
	var defaultOption = {
		format : "yyyy-mm-dd",
		startDate: "2016-06-05",
		endDate  : "2018-12-05",
        separator : ' 至 ',
        applyLabel : "确定",
        cancleLabel : "取消",
        onShow :function(){},
        onApplyHandler: function(){},
        onCancelHandler: function(){}
	}	
	var DateRangpicker = function(element,options){
		options = options || {};
		this.options = $.extend({}, defaultOption,options) ;
		this.parentEl = $('body');
        this.element = $(element);
        this.monthsShort = DateConfig.monthsShort;
        this.format    = DateConfig.parseFormat(this.options.format);
		this.startDate =  DateConfig.parseDate(this.options["startDate"],this.format); 
		this.endDate   =  DateConfig.parseDate(this.options["endDate"],this.format); 
		
		
		this.selectStartDate = this.startDate;
		this.selectEndDate = this.endDate;
		
		
		this.startYear =  this.startDate.getFullYear();
		this.endYear   =  this.endDate.getFullYear();
		this.container = $(DateConfig.dateTemplete).appendTo(this.parentEl);
		this.separator = this.options["separator"]
		this.initRender();
		this.initMonthRender();
		this.initYearCheck();
		this.showSelectDate(); //显示默认时间
		this.checkInitDateTime();
	}
	DateRangpicker.prototype = {
		constructor : DateRangpicker,
		initRender :function(){
			var leftPanel   = this.container.find("div.leftPanel");
			var rightPanel  = this.container.find("div.rightPanel");
			this.leftYears   = leftPanel.find("div.yearLists")
			this.leftMonths  = leftPanel.find("div.monthContainer");
			this.rightYears  = rightPanel.find("div.yearLists")
			this.rightMonths = rightPanel.find("div.monthContainer");
			this.applyBtn  = $(this.container).find("button.applyBtn").html(this.options.applyLabel);
			this.cancleBtn = $(this.container).find("button.cancelBtn").html(this.options.cancleLabel);
			this.leftYears.html(this.getRenderYear("left"));
			this.rightYears.html(this.getRenderYear("right"));
			rightPanel.on({click: $.proxy(this.clickHandler, this,"right")});
			leftPanel.on({click: $.proxy(this.clickHandler, this,"left")});
			this.element.on({
                click: $.proxy(this.showPanel, this),
                focus: $.proxy(this.showPanel, this)
            });
			this.applyBtn.on({click: $.proxy(this.applyHandler, this)});
			this.cancleBtn.on({click: $.proxy(this.cancenHandler, this)});
		},
		setStartDate :function(startDate){
			this.startDate =  DateConfig.parseDate(this.options["startDate"],this.format); 
			this.showSelectDate(); //显示默认时间
		},
		setEndDate :function(endDate){
			this.endDate   =  DateConfig.parseDate(this.options["endDate"],this.format); 
			this.showSelectDate(); //显示默认时间
		},
		applyHandler:function(e){
	        this.hidePanel();
	        var onApplyHandler = this.options["onApplyHandler"];
	        if(onApplyHandler && $.isFunction(onApplyHandler)){
	        	    var valid = this.checkDateValid();
	        	    if(valid){
	        	    		onApplyHandler.apply(this.element,[this.getStartDate(),this.getEndDate(),this]);
	        	    }else{
	        	    		onCancelHandler.apply(this.element,["","",this]);
	        	    }
	        }
		},
		cancenHandler:function(e){
			this.hidePanel();
	        var onCancelHandler = this.options["onCancelHandler"];
	        if(onCancelHandler && $.isFunction(onCancelHandler)){
	        		onCancelHandler.apply(this.element,[this,e]);
	        }
		},
		initMonthRender: function(){
			var html ="",i = 0;
			var monthsShort =  this.monthsShort;
			while (i < 12) {
				html += '<span class="month" date="'+i+'">'+monthsShort[i++]+'</span>';
			}
			var startMonth = this.startDate.getMonth();
			var endMonth   = this.endDate.getMonth();
			this.leftMonths.html(html);
			this.leftMonths.find("span[date="+startMonth+"]").addClass("active");
			this.rightMonths.html(html);
			this.rightMonths.find("span[date="+endMonth+"]").addClass("active");
		},
		getRenderYear: function(type){
			var yearData = (type == "left"?this.startDate :this.endDate).getFullYear();
			var year = parseInt(yearData/10, 10) * 10;
			var html = '',everyYear = year -1 ;
			for (var i = 0; i < 10; i++) {
				html += '<span class="year" date="'+everyYear+'">'+everyYear+'</span>';
				everyYear += 1;
			}
			return html;
		},
		enableApplyBtn :function(){
			this.applyBtn.removeClass("disableBtn");
		},
		disApplyBtn :function(){
			this.applyBtn.addClass("disableBtn");
		},
		initYearCheck :function(){
			this.leftYears.find("span[date="+this.startYear+"]").addClass("active");
			this.rightYears.find("span[date="+this.endYear+"]").addClass("active")
		},
		getStartDate :function(){
			var years = this.startDate.getFullYear();
			var month = this.startDate.getMonth()+1;
			month = month<10 ? "0"+ month : month;
			return years +"-" +month;
		},
		getEndDate :function(){
			var years = this.endDate.getFullYear();
			var month = this.endDate.getMonth()+1;
			month = month<10 ? "0"+ month : month;
			return years +"-" +month;
		},
	    //显示选中的日期
		showSelectDate :function(){
	        var startDate = this.getStartDate();
	        var endDate   = this.getEndDate();
	        var date = startDate +this.separator +endDate;
			$(this.container).find(".startEndPickerDate").html(date);
		},
		//初始化日期
		checkInitDateTime : function(){
			var valid  = this.checkDateValid();
			if(valid){
				this.enableApplyBtn();
			}else{
				this.disApplyBtn();
			}
		},
		//判断日期
		checkDateValid:function(){
			return (this.endDate.getTime() > this.startDate.getTime()) ?true :false;
		},
		upNextHandler:function(target,type){
			 var prevNext = $(target).hasClass("prev") ? -1 :1;
			 if(type =="left"){
			 	var newYear = this.startDate.getFullYear() + parseInt(prevNext) * 10;
			 	this.startDate.setFullYear(newYear);
			 	this.leftYears.html(this.getRenderYear(type));
			 }else{
			 	var newEndYear = this.endDate.getFullYear() + parseInt(prevNext) * 10;
			 	this.endDate.setFullYear(newEndYear);
				this.rightYears.html(this.getRenderYear(type));
			 }
			 this.initYearCheck();//当前时间被选中
		},
		//判断日期区间大小
		checkSelectDate :function(target,type){
			 var dataValue = parseInt($(target).attr("date"));
			 if(type =="left"){
			 	 if($(target).hasClass("year")){
			 	 	this.selectStartDate.setFullYear(dataValue);
			 	 }else{
			 	 	this.selectStartDate.setMonth(dataValue);
			 	 }
			 }else{
			 	 if($(target).hasClass("year")){
			 	 	this.selectEndDate.setFullYear(dataValue);
			 	 }else{
			 	 	this.selectEndDate.setMonth(dataValue);
			 	 }
			 }
			  //旋转无效
	 	     var isValid = this.checkDateValid();
	 	     if(!isValid){
	 	     	return false;
	 	     }
	 	     //正确的话
 	     	 this.startDate = this.selectStartDate;
 	     	 this.endDate = this.selectEndDate;
	 	     return true;
		},
		//选中日期的判断
		selectedHandler : function(target,type){
			 if(!this.checkSelectDate(target,type)){
			 	this.disApplyBtn();
	 	     	return false;
			 }
	 	     this.startYear =  this.startDate.getFullYear();
			 this.endYear   =  this.endDate.getFullYear();
			 $(target).addClass("active").siblings().removeClass('active');
			 this.showSelectDate();
			 this.enableApplyBtn();
		},
		clickHandler : function(type,event){
			var target  = $(event.target);
			//选中月份 年份
			if($(target).hasClass("month") || $(target).hasClass("year")){
				this.selectedHandler(target,type);
			}else if ($(target).hasClass("prev")  || $(target).hasClass("next")){
				this.upNextHandler(target,type);
			}
		},
		movePostion: function () {
            var eleHeight = this.element.outerHeight();
            this.container.css({
                top:  this.element.offset().top + this.element.outerHeight()+1,
                left: this.element.offset().left
            });
        },
        hidePanel:function(){
        		$(document).off('click', function(){});
            this.container.hide();
        },
        outsideClick :function(e){
            var target = $(e.target);
            if (target.closest(this.element).length ||  target.closest(this.container).length){
            	  return;
            }
            this.hidePanel();
        },
        showPanel:function(){
        		this.container.show();
        		this.movePostion();
        		$(document).on('click', $.proxy(this.outsideClick, this));
        }
	}
	
	
	
	$.fn.dateYMRangPicker = function ( option, val ) {
		return this.each(function () {
			var $this = $(this),data = $this.data('datepicker'),
			options = typeof option === 'object' && option;
			if (!data) {
				$this.data('datepicker', (data = new DateRangpicker(this, option)));
			}
			if (typeof option === 'string') data[option](val);
		});
	};
	
	
})(jQuery);
