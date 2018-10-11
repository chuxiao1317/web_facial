$(function () {
    //退出管理员页面
    $("#logoutDiv").click(function () {
        $(window).attr("location", "/facial/logout");
    });

    // 进来先查一下
    $.ajax({
        type: "get",
        url: '/facial/getOrganizationsNoCondition',
        success: function (html) {
            $("#contentDiv").append(html);
            $("#pagingUl > li").css({"color": "teal", "background": "white"});
            $("#pagingUl > li:first").css({"color": "white", "background": "teal"});
            $("#firstPageLi").css({"color": "white", "background": "teal"});
        }
    });

    // 分页查看users刷脸记录
    $("#clockingsMenu").click(function () {
        $.ajax({
            type: "get",
            url: '/facial/getUserClockingsNoCondition',
            success: function (html) {
                $("#clockingsMenu").css("background", "gray");
                $("#organizationsMenu").css("background", "silver");
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                // $("#pagingUl > li").css({"color": "teal", "background": "white"});
                // $("#pagingUl > li:first").css({"color": "white", "background": "teal"});
                // $("#firstPageLi").css({"color": "white", "background": "teal"});
            }
        })
    });

    // 班级管理分页查询
    $("#organizationsMenu").click(function () {
        $.ajax({
            type: "get",
            url: '/facial/getOrganizationsNoCondition',
            success: function (html) {
                $("#organizationsMenu").css("background", "gray");
                $("#clockingsMenu").css("background", "silver");
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                $("#pagingUl > li:first").css({"color": "white", "background": "teal"});
                $("#firstPageLi").css({"color": "white", "background": "teal"});
            }
        })
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
            url: '/facial/getOrganizationsNoCondition',
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
            url: "/facial/getOrganizationsNoCondition?page=" + (organizationsTotalPages - 1),
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
            url: "/facial/getOrganizationsNoCondition?",
            data: {
                "page": organizationsNowPageNum - 1
            },
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                // 此时organizationsNowPageNum已经-1
                $("#pagingUl > li").eq(organizationsNowPageNum).css({"color": "white", "background": "teal"});
                if (organizationsNowPageNum == 0) {
                    $("#firstPageLi").css({"color": "white", "background": "teal"});
                }
            }

        })
    });

    // 查看下一页
    $(document).on("click", "#nextPageLi", function () {
        $.ajax({
            type: "get",
            url: "/facial/getOrganizationsNoCondition?",
            data: {
                "page": organizationsNowPageNum + 1
            },
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                // 此时organizationsNowPageNum已经+1
                $("#pagingUl > li").eq(organizationsNowPageNum).css({"color": "white", "background": "teal"});
                if (organizationsNowPageNum == organizationsTotalPages - 1) {
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
            url: "/facial/getOrganizationsNoCondition?page=" + (pageNum - 1),
            success: function (html) {
                $("#contentDiv").empty();
                $("#contentDiv").append(html);
                $("#pagingUl > li").css({"color": "teal", "background": "white"});
                $("#pagingUl > li").eq(pageNum - 1).css({"color": "white", "background": "teal"});
                if (pageNum == 1) {
                    $("#firstPageLi").css({"color": "white", "background": "teal"});
                } else if (pageNum == organizationsTotalPages) {
                    $("#lastPageLi").css({"color": "white", "background": "teal"});
                }
            }
        })
    });

    // 添加班级
    $(document).on("click", "#addOrganizationButton", function () {
        $("#addOrganizationInput").css("display", "block");
        $("#saveAddOrganizationBtn").css("display", "block");
        $("#saveAddOrganizationBtn").click(function () {
            // 若输入框为空则不进行添加操作
            if ($("#addOrganizationInput").val() == null || $("#addOrganizationInput").val().length <= 0) {
                $("#addOrganizationInput").css("display", "none");
                $("#saveAddOrganizationBtn").css("display", "none");
            } else {
                $.ajax({
                    type: "post",
                    url: "/facial/addOrganization",
                    data: {
                        "organizationName": $("#addOrganizationInput").val()
                    }, success: function (success) {
                        if (success) {
                            $.ajax({
                                type: "get",
                                url: '/facial/getOrganizationsNoCondition',
                                success: function (html) {
                                    // 刷新布局
                                    $("#contentDiv").empty();
                                    $("#contentDiv").append(html);
                                    $("#pagingUl > li").css({"color": "teal", "background": "white"});
                                    $("#pagingUl > li:first").css({"color": "white", "background": "teal"});
                                    $("#firstPageLi").css({"color": "white", "background": "teal"});
                                }
                            });
                        } else {
                            alert("添加失败");
                            $("#addOrganizationInput").css("display", "none");
                            $("#saveAddOrganizationBtn").css("display", "none");
                        }
                    }
                })
            }
        })
    });

    // 修改班级名称
    $(document).on("click", ".compileDiv", function () {
        const $thisTr = $(this).parent().parent();
        const $thisTrInput = $thisTr.find(".compileOrganizationInput");
        const $thisTrBtn = $thisTr.find(".compileOrganizationBtn");
        $thisTrInput.css("display", "block");
        $thisTrBtn.css("display", "block");
        $thisTrBtn.click(function () {
            if ($thisTrInput.val() == null || $thisTrInput.val().length <= 0) {
                $thisTrInput.css("display", "none");
                $thisTrBtn.css("display", "none");
            } else {
                $.ajax({
                    type: "post",
                    url: "/facial/updateOrganization",
                    data: {
                        "newName": $thisTrInput.val(),
                        "previousName": $thisTr.find(".compileOrganizationNameThDiv").html()
                    },
                    success: function (success) {
                        if (success) {
                            $.ajax({
                                type: "get",
                                url: '/facial/getOrganizationsNoCondition',
                                success: function (html) {
                                    // 刷新布局
                                    $("#contentDiv").empty();
                                    $("#contentDiv").append(html);
                                    $("#pagingUl > li").css({"color": "teal", "background": "white"});
                                    $("#pagingUl > li:first").css({"color": "white", "background": "teal"});
                                    $("#firstPageLi").css({"color": "white", "background": "teal"});
                                }
                            });
                        } else {
                            alert("修改失败");
                            $thisTrInput.css("display", "none");
                            $thisTrBtn.css("display", "none");
                        }
                    }
                })
            }
        });
    });

    // 删除班级
    $(document).on("click", ".deleteDiv", function () {
        $.ajax({
            type: "post",
            url: "/facial/deleteOrganization",
            data: {
                "organizationName": $(this).parent().parent().find(".compileOrganizationNameThDiv").html()
            },
            success: function (success) {
                if (success) {
                    $.ajax({
                        type: "get",
                        url: '/facial/getOrganizationsNoCondition',
                        success: function (html) {
                            // 刷新布局
                            $("#contentDiv").empty();
                            $("#contentDiv").append(html);
                        }
                    });
                } else {
                    alert("删除失败");
                }
            }
        })
    });

    // $("#analizeMenu").click(function () {
    //     $(window).attr('location', 'analyze.html');
    // });

    // 编辑页面跳转
    $(".compileDiv").click(function () {
        $(window).attr('location', 'form.html');
    });

    // 收入查询or支出查询切换
    $("#payIncome_Select").click(function () {
        if ($(this).val() == "收入查询") {
            $("#payIncome_show").text("收入记录");
        } else {
            $("#payIncome_show").text("支出记录");
        }
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

    try {
        // 收支分析图:饼图
        var ctx = $("#pie").get(0).getContext("2d");

        var data = {
            datasets: [{
                data: [20, 15],
                backgroundColor: [
                    'red',
                    'blue'
                ]
            }],
            labels: [
                '收入',
                '支出'
            ],
        };

        var myPieChart = new Chart(ctx, {
            type: 'pie',
            data: data,
            options: {
                title: {
                    display: true,
                    text: '收支比例分析图'
                }
            }
        });

        // 收入曲线图
        var ctx1 = $("#lineIncome").get(0).getContext("2d");

        var data1 = {
            labels: ["January", "February", "March", "April", "May", "June", "July"],
            datasets: [{
                label: "收入金额",
                fillColor: "rgba(220,220,220,0.5)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                data: [65, 59, 90, 81, 56, 55, 40]
            }]
        }

        var stackedLine = new Chart(ctx1, {
            type: 'line',
            data: data1,
            options: {
                scales: {
                    yAxes: [{
                        stacked: true
                    }]
                },
                title: {
                    display: true,
                    text: '收入金额曲线图'
                }
            }
        });

        //支出曲线图
        var ctx2 = $("#linePay").get(0).getContext("2d");

        var data2 = {
            labels: ["January", "February", "March", "April", "May", "June", "July"],
            datasets: [{
                label: "支出金额",
                fillColor: "rgba(220,220,220,0.5)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                data: [20, 90, 93, 35, 40, 55, 82]
            }]
        }

        var stackedLine = new Chart(ctx2, {
            type: 'line',
            data: data2,
            options: {
                scales: {
                    yAxes: [{
                        stacked: true
                    }]
                },
                title: {
                    display: true,
                    text: '支出金额曲线图'
                }
            }
        });
    } catch (e) {
        if (e instanceof TypeError) {
        }
    }
});