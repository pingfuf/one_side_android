/**
 * 定义一些基本的JS函数
 *
 */
/**
 *
 */
function DomUtils() {

}

DomUtils.prototype.isDomIdVisible: function(id) {
    var dom  = $("#" + id);
    var display = dom.css("display");
    var visibility = dom.css("visibility");

    return !(display == "none" || visibility == "hidden")
}