$(function () {

    // $("#queryMyClockingsUrl").click(function () {
    //     $.ajax({
    //         type: "get",
    //         url: "/facial/queryMyClockings",
    //         success: function (data) {
    //             console.log(data)
    //             for (var i = 0; i < data.length; i++) {
    //                 $("#MyuserClockingsTable").append("<tr><td>" + data[i] + " </td></tr>");
    //             }
    //         },
    //         error: function (result) {
    //             var response = result.responseText;
    //             alert('Error loading: ' + response);
    //         }
    //     })
    // })

    //退出管理员页面
    $("#logoutDiv").click(function () {
        $(window).attr("location", "/facial/logout");
    });

    // 进来先查一下
    $.ajax({
        type: "get",
        url: '/facial/queryMyClockings',
        success: function (html) {
            $("#contentDiv").append(html);
            $("#pagingUl > li").css({"color": "teal", "background": "white"});
            $("#pagingUl > li:first").css({"color": "white", "background": "teal"});
            $("#firstPageLi").css({"color": "white", "background": "teal"});
        }
    });

    // 首页、尾页、上一页、下一页鼠标点击样式调整
    $(document).on("mousedown", "#firstPageLi", function () {
        $("#firstPageLi").css({"color": "white", "background": "teal"})
    });
    $(document).on("mouseup", "#firstPageLi", function () {
        $("#firstPageLi").css({"color": "teal", "background": "white"})
    });
    $(document).on("mousedown", "#lastPageLi", function () {
        $("#lastPageLi").css({"color": "white", "background": "teal"})
    });
    $(document).on("mouseup", "#lastPageLi", function () {
        $("#lastPageLi").css({"color": "teal", "background": "white"})
    });
    $(document).on("mousedown", "#previousPageLi", function () {
        $("#previousPageLi").css({"color": "white", "background": "teal"})
    });
    $(document).on("mouseup", "#previousPageLi", function () {
        $("#previousPageLi").css({"color": "teal", "background": "white"})
    });
    $(document).on("mousedown", "#nextPageLi", function () {
        $("#nextPageLi").css({"color": "white", "background": "teal"})
    });
    $(document).on("mouseup", "#nextPageLi", function () {
        $("#nextPageLi").css({"color": "teal", "background": "white"})
    });

    // 查看班级首页
    $(document).on("click", "#firstPageLi", function () {
        $.ajax({
            type: "get",
            url: '/facial/queryMyClockings',
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                $("#pagingUl > li:first").css({"color": "white", "background": "teal"});
                $("#firstPageLi").css({"color": "white", "background": "teal"});
            }
        })
    });

    // 查看班级尾页
    $(document).on("click", "#lastPageLi", function () {
        $.ajax({
            type: "get",
            url: "/facial/queryMyClockings?page=" + (createDatesTotalPages - 1),
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                $("#pagingUl > li:last").css({"color": "white", "background": "teal"});
                $("#lastPageLi").css({"color": "white", "background": "teal"});
            }
        });
    });

    // 查看上一页
    $(document).on("click", "#previousPageLi", function () {
        $.ajax({
            type: "get",
            url: "/facial/queryMyClockings",
            data: {
                "page": createDatesNowPageNum - 1
            },
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                // 此时organizationsNowPageNum已经-1
                $("#pagingUl > li").eq(createDatesNowPageNum).css({"color": "white", "background": "teal"});
                if (createDatesNowPageNum == 0) {
                    $("#firstPageLi").css({"color": "white", "background": "teal"});
                }
            }

        })
    });

    // 查看下一页
    $(document).on("click", "#nextPageLi", function () {
        $.ajax({
            type: "get",
            url: "/facial/queryMyClockings",
            data: {
                "page": createDatesNowPageNum + 1
            },
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                // 此时organizationsNowPageNum已经+1
                $("#pagingUl > li").eq(createDatesNowPageNum).css({"color": "white", "background": "teal"});
                if (createDatesNowPageNum == createDatesTotalPages - 1) {
                    $("#lastPageLi").css({"color": "white", "background": "teal"});
                }
            }

        })
    });

    // 点击页数查看班级
    $(document).on("click", "#pagingUl > li", function () {
        const pageNum = $(this).text();
        $.ajax({
            type: "get",
            url: "/facial/queryMyClockings?page=" + (pageNum - 1),
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                $("#pagingUl > li").eq(pageNum - 1).css({"color": "white", "background": "teal"});
                if (pageNum == 1) {
                    $("#firstPageLi").css({"color": "white", "background": "teal"});
                } else if (pageNum == createDatesTotalPages) {
                    $("#lastPageLi").css({"color": "white", "background": "teal"});
                }
            }
        })
    });

    // 窗口宽度自适应
    $(window).resize(function () {
        $("#bigDiv").css("width", "100%");
        $("#contentDiv").css("width", "88%");
    });

    // 窗口高度自适应:初始值
    $("#bigDiv").css("height", $(window).height());
    $("#menuDiv").css("height", $("#bigDiv").height() - 70);
    $("#contentDiv").css("height", $("#bigDiv").height() - 70);
    $("#imgDiv").css("margin-top", $("#menuDiv").height() - 226);

    // 窗口高度自适应：改变窗口大小的时候
    $(window).resize(function () {
        $("#bigDiv").css("height", $(window).height());
        $("#menuDiv").css("height", $("#bigDiv").height() - 70);
        $("#contentDiv").css("height", $("#bigDiv").height() - 70);
        $("#imgDiv").css("margin-top", $("#menuDiv").height() - 226);
    });

})