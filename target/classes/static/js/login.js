$(function () {

    var loginRoleFlag = "user";

    $("#adminLoginDiv").click(function () {
        $("#adminLoginDiv").css({"background": "#3f89ec", "color": "white"});
        $("#userLoginDiv").css({"background": "#ccc", "color": "black"});
        loginRoleFlag = "admin";
    })

    $("#userLoginDiv").click(function () {
        $("#userLoginDiv").css({"background": "#3f89ec", "color": "white"});
        $("#adminLoginDiv").css({"background": "#ccc", "color": "black"});
        loginRoleFlag = "user";
    })

    $("#registerLinkDiv").click(function () {
        $(window).attr("location", "/facial/enrollPage");
    })

    // 用户名有效验证
    $("#usernameInput").blur(function () {
        if ($("#usernameInput").val().length < 6 && $("#usernameInput").val().length > 0) {
            $("#usernameError").text("用户名不能小于6位！");
        } else if ($("#usernameInput").val().length == 0) {
            $("#usernameError").text("用户名不能为空！");
        }
    });
    // 登录密码有效验证
    $("#passwordInput").blur(function () {
        if ($("#passwordInput").val().length < 6 && $("#passwordInput").val().length > 0) {
            $("#passwordError").text("用户名不能小于6位！");
        } else if ($("#passwordInput").val().length == 0) {
            $("#passwordError").text("密码不能为空！");
        }
    });
    // 获得焦点清空验证警告
    $("#usernameInput").focus(function () {
        $("#usernameError").text("");
    });
    $("#passwordInput").focus(function () {
        $("#passwordError").text("");
    });

    /**
     * 登录
     * */
    $("#loginButton").click(function () {
        if (loginRoleFlag == "user") {
            $.ajax({
                type: "post",
                url: "/facial/userLogin",
                data: {
                    "username": $("#usernameInput").val(),
                    "password": $("#passwordInput").val()
                },
                dataType: "json",
                success: function (data) {
                    var msg = $.parseJSON(data);
                    if (msg) {
                        $(window).attr("location", "/facial/user");
                    } else {
                        alert("登录失败！");
                    }
                }

            })
        } else if (loginRoleFlag == "admin") {
            $.ajax({
                type: "post",
                url: "/facial/adminLogin",
                data: {
                    "username": $("#usernameInput").val(),
                    "password": $("#passwordInput").val()
                },
                dataType: "json",
                success: function (data) {
                    var msg = $.parseJSON(data);
                    if (msg) {
                        $(window).attr("location", "/facial/admin");
                    } else {
                        alert("登录失败！");
                    }
                }

            })
        }
    })
});