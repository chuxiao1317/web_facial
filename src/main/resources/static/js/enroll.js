window.onload = function () {
    /**
     * 注册信息验证标记
     * */
    var usernameCheckFlag = false;
    var passwordCheckFlag = false;
    var nicknameCheckFlag = false;

    /**
     * 获得焦点取消验证
     * */
    $("#usernameInput").focus(function () {
        $("#userVerify").text("");
        $("#enrollResult").html("")
    });
    $("#passwordInput").focus(function () {
        $("#passwordVerify").text("");
        $("#repetitionPwVerify").text("");
        $("#enrollResult").text("")
    });
    $("#repetitionPw").focus(function () {
        $("#repetitionPwVerify").text("");
        $("#enrollResult").text("")
    });
    $("#nicknameInput").focus(function () {
        $("#nicknameVerify").text("");
        $("#enrollResult").text("")
    });

    /**
     * 用户名有效验证
     * */
    $("#usernameInput").blur(function () {
        if ($("#usernameInput").val().length < 6 && $("#usernameInput").val().length > 0) {
            $("#userVerify").text("用户名不得少于6位！");
            $("#userVerify").css("color", "red");
            $("#camera").attr("disabled", true);
            $("#snap").attr("disabled", true);
            $("#registerButton").attr("disabled", true);
            $("#registerButton").css({"background": "silver", "border": "silver"});
            usernameCheckFlag = false;
        } else if ($("#usernameInput").val().length <= 0) {
            $("#userVerify").text("用户名不能为空！");
            $("#userVerify").css("color", "red");
            $("#camera").attr("disabled", true);
            $("#snap").attr("disabled", true);
            $("#registerButton").attr("disabled", true);
            $("#registerButton").css({"background": "silver", "border": "silver"});
            usernameCheckFlag = false;
        } else {
            $.ajax({
                type: "post",
                url: "/facial/checkUsernameUniqueness",
                data: {
                    "username": $("#usernameInput").val()
                },
                dataType: "json",
                success: function (data) {
                    var msg = $.parseJSON(data);
                    if (msg) {
                        $("#userVerify").text("√");
                        $("#userVerify").css("color", "green");
                        // 解除摄像头按钮禁用状态
                        $("#camera").attr("disabled", false);
                        usernameCheckFlag = true;
                    } else {
                        $("#userVerify").text("该账号已被注册！");
                        $("#userVerify").css("color", "red");
                        $("#camera").attr("disabled", true);
                        $("#snap").attr("disabled", true);
                        $("#registerButton").attr("disabled", true);
                        $("#registerButton").css({"background": "silver", "border": "silver"});
                        usernameCheckFlag = false;
                    }
                },
                error: function (data) {
                    $("#camera").attr("disabled", true);
                    $("#snap").attr("disabled", true);
                    $("#registerButton").attr("disabled", true);
                    $("#registerButton").css({"background": "silver", "border": "silver"});
                    usernameCheckFlag = false;
                    alert("error!数据格式：" + typeof data);
                }
            })
        }
    });

    /**
     * 密码有效验证
     * */
    $("#passwordInput").blur(function () {
        if ($("#passwordInput").val().length < 6 && $("#passwordInput").val().length > 0) {
            $("#passwordVerify").text("密码不得少于6位！");
            $("#passwordVerify").css("color", "red");
            $("#registerButton").attr("disabled", true);
            $("#registerButton").css({"background": "silver", "border": "silver"});
            passwordCheckFlag = false;
        } else if ($("#passwordInput").val().length <= 0) {
            $("#passwordVerify").text("密码不能为空！");
            $("#passwordVerify").css("color", "red");
            $("#registerButton").attr("disabled", true);
            $("#registerButton").css({"background": "silver", "border": "silver"});
            passwordCheckFlag = false;
        } else {
            if ($("#repetitionPw").val() != $("#passwordInput").val() && $("#repetitionPw").val().length > 0) {
                $("#repetitionPwVerify").text("两次输入的密码不一致！");
                $("#repetitionPwVerify").css("color", "red");
                $("#registerButton").attr("disabled", true);
                $("#registerButton").css({"background": "silver", "border": "silver"});
                passwordCheckFlag = false;
            } else if ($("#repetitionPw").val() == $("#passwordInput").val()) {
                $("#passwordVerify").text("√");
                $("#passwordVerify").css("color", "green");
                $("#repetitionPwVerify").text("√");
                $("#repetitionPwVerify").css("color", "green");
                passwordCheckFlag = true;
            } else {
                $("#passwordVerify").text("√");
                $("#passwordVerify").css("color", "green");
                passwordCheckFlag = true;
            }
        }
    });

    /**
     * 重复密码有效验证
     * */
    $("#repetitionPw").blur(function () {
        if ($("#passwordInput").val().length >= 6) {
            if ($("#repetitionPw").val().length <= 0) {
                $("#repetitionPwVerify").text("请确认您的密码！");
                $("#repetitionPwVerify").css("color", "red");
                $("#registerButton").attr("disabled", true);
                $("#registerButton").css({"background": "silver", "border": "silver"});
                passwordCheckFlag = false;
            } else {
                if ($("#repetitionPw").val() != $("#passwordInput").val()) {
                    $("#repetitionPwVerify").text("两次输入的密码不一致！");
                    $("#repetitionPwVerify").css("color", "red");
                    $("#registerButton").attr("disabled", true);
                    $("#registerButton").css({"background": "silver", "border": "silver"});
                    passwordCheckFlag = false;
                } else {
                    if ($("#passwordInput").val().length >= 6) {
                        $("#passwordVerify").text("√");
                        $("#passwordVerify").css("color", "green");
                        $("#repetitionPwVerify").text("√");
                        $("#repetitionPwVerify").css("color", "green");
                        passwordCheckFlag = true;
                    }
                }
            }
        }
    });

    /**
     * 昵称验证
     * */
    $("#nicknameInput").blur(function () {
        if ($("#nicknameInput").val().length <= 0) {
            $("#nicknameVerify").text("昵称不能为空！");
            $("#nicknameVerify").css("color", "red");
            $("#registerButton").attr("disabled", true);
            $("#registerButton").css({"background": "silver", "border": "silver"});
            nicknameCheckFlag = false;
        } else {
            $("#nicknameVerify").text("√");
            $("#nicknameVerify").css("color", "green");
            nicknameCheckFlag = true;
        }
    })

    /**
     * 开启摄像头
     * */
    const video = document.querySelector('#video');
    const snap = document.querySelector('#snap');
    const canvas = document.querySelector('#canvas');
    $("#camera").click(function () {
        var promisifiedOldGUM = function (constraints) {
            var getUserMedia = (navigator.getUserMedia ||
                navigator.webkitGetUserMedia ||
                navigator.mozGetUserMedia);
            if (!getUserMedia) {
                return Promise.reject(new Error('getUserMedia is not implemented in this browser'));
            }
            return new Promise(function (resolve, reject) {
                getUserMedia.call(navigator, constraints, resolve, reject);
            });
        }
        if (navigator.mediaDevices === undefined) {
            navigator.mediaDevices = {};
        }
        if (navigator.mediaDevices.getUserMedia === undefined) {
            navigator.mediaDevices.getUserMedia = promisifiedOldGUM;
        }

        // Prefer camera resolution nearest to 1280x720.
        var constraints = {audio: true, video: {width: 1280, height: 720}};

        navigator.mediaDevices.getUserMedia(constraints)
            .then(function (stream) {
                var video = document.querySelector('video');
                video.src = window.URL.createObjectURL(stream);
                video.onloadedmetadata = function (e) {
                    video.play();
                };
            })
            .catch(function (err) {
                console.log(err.name + ": " + err.message);
            });

        /**
         * 点击按钮拍照并入库
         * */
        $("#snap").attr("disabled", false);// 解除拍照按钮禁用状态
        snap.addEventListener('click', function () {
            var context = canvas.getContext('2d');
            var img = context.drawImage(video, 0, 0, 400, 300);

            //尝试入库
            var imgData = document.getElementById("canvas").toDataURL("image/png");
            data = imgData.substr(22);
            $.ajax({
                type: "post",
                url: "/facial/canEnroll",
                data: {
                    data: data,
                    "username": $("#usernameInput").val()
                },
                success: function (data) {
                    var msg = $.parseJSON(data);
                    if (msg) {
                        $("#enrollResult").html("入库成功！");
                        $("#enrollResult").css("color", "green");
                        if (usernameCheckFlag && passwordCheckFlag && nicknameCheckFlag) {
                            // 解除注册按钮禁用状态
                            $("#registerButton").attr("disabled", false);
                            $("#registerButton").css({"background": "#3f89ec", "border": "#3f89ec"})
                        } else {
                            $("#registerButton").attr("disabled", true);
                            $("#registerButton").css({"background": "silver", "border": "silver"});
                        }
                    } else {
                        $("#enrollResult").html("入库失败,请重试！");
                        $("#enrollResult").css("color", "red");
                        $("#registerButton").attr("disabled", true);
                        $("#registerButton").css({"background": "silver", "border": "silver"});
                    }
                },
                error: function () {
                    alert("error!");
                }
            })
        })
    })

    // $("#saveButton").click(function () {
    //     var saveHref = document.getElementById("saveButton");
    //     saveHref.href = canvas.toDataURL("image/png");
    //
    // })

    $("#registerButton").click(function () {
        $("#registerButton").attr("disabled", true);
        $("#registerButton").css({"background": "silver", "border": "silver"});
        $.ajax({
            type: "post",
            url: "/facial/register",
            data: {
                data: data,
                "username": $("#usernameInput").val(),
                "password": $("#passwordInput").val(),
                "nickname": $("#nicknameInput").val(),
                "sex": $("input[name='sex']:checked").val(),
                "organization": $("#organization").val()
            },
            dataType: "json",
            success: function (data) {
                var msg = $.parseJSON(data);
                if (msg) {
                    $(window).attr("location", "/facial/enrollSuccess");
                } else {
                    alert("注册失败！")
                }
            },
            error: function () {
                alert("error!");
            }
        })
    })
}