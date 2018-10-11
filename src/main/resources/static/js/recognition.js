$(function () {
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


        // 点击按钮之后拍照
        // canvas 可以放图片 stram 流里取出一张
        // 先解除拍照按钮的禁用状态
        $("#snap").attr("disabled", false);
        snap.addEventListener('click', function () {
            var context = canvas.getContext('2d');
            context.drawImage(video, 0, 0, 400, 300);

            /**
             * 初始化识别按钮重复点击时间间隔
             * */
            var cameraTimeout = 0;
            var timeoutFunction = null;

            // if (cameraTimeout <= 0) {
            // 解除识别按钮禁用状态
            $("#recognitionButton").attr("disabled", false);
            // 识别
            $("#recognitionButton").click(function () {
                $("#recognitionButton").attr("disabled", true);
                cameraTimeout = 3;
                timeoutFunction = setInterval(function () {
                    cameraTimeout--;
                    $("#recognitionButton").text(cameraTimeout + "s");
                    if (cameraTimeout <= 0) {
                        $("#recognitionButton").attr("disabled", false);
                        $("#recognitionButton").text("识别");
                        clearInterval(timeoutFunction);
                        return;
                    }
                }, 1000);
                $("#recognitionResult").html("&nbsp;");
                var imgData = document.getElementById("canvas").toDataURL("image/png");
                var data = imgData.substr(22);
                $.ajax({
                    type: "post",
                    url: "/facial/recognition",
                    data: {
                        data: data
                    },
                    success: function (msg) {
                        $("#recognitionResult").html(msg);
                    },
                    error: function () {
                        alert("error!");
                    }
                })
            })
        })
    })
})