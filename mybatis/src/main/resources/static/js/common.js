function p(obj) {
    console.info(obj);
}

// 自定义校验
$.extend($.fn.validatebox.defaults.rules, {
    idcard: { 
        validator: function(value) {
            return true;
        },
        message: '测试'
    }
});
