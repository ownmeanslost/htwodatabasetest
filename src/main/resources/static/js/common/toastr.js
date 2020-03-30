var messageIframe;
var welcomeToa = null;
(function (define) {
    define(['jquery'],
        function ($) {
            return (function () {
                var $container;
                var listener;
                var toastId = 0;
                var toastType = {
                    error: 'error',
                    info: 'info',
                    success: 'success',
                    warning: 'warning'
                };
                var toastr = {
                    clear: clear,
                    remove: remove,
                    error: error,
                    getContainer: getContainer,
                    info: info,
                    options: {},
                    subscribe: subscribe,
                    success: success,
                    version: '2.0.3',
                    warning: warning
                };
                return toastr;

                function error(message, title, optionsOverride) {
                    return notify({
                        type: toastType.error,
                        iconClass: getOptions().iconClasses.error,
                        message: message,
                        optionsOverride: optionsOverride,
                        title: title
                    });
                }

                function getContainer(options, create) {
                    if (!options) {
                        options = getOptions();
                    }
                    $container = $('#' + options.containerId);
                    if ($container.length) {
                        return $container;
                    }
                    if (create) {
                        $container = createContainer(options);
                    }
                    return $container;
                }

                function info(message, title, optionsOverride) {
                    return notify({
                        type: toastType.info,
                        iconClass: getOptions().iconClasses.info,
                        message: message,
                        optionsOverride: optionsOverride,
                        title: title
                    });
                }

                function subscribe(callback) {
                    listener = callback;
                }

                function success(message, title, optionsOverride) {
                    return notify({
                        type: toastType.success,
                        iconClass: getOptions().iconClasses.success,
                        message: message,
                        optionsOverride: optionsOverride,
                        title: title
                    });
                }

                function warning(message, title, optionsOverride) {
                    return notify({
                        type: toastType.warning,
                        iconClass: getOptions().iconClasses.warning,
                        message: message,
                        optionsOverride: optionsOverride,
                        title: title
                    });
                }

                function clear($toastElement) {
                    var options = getOptions();
                    if (!$container) {
                        getContainer(options);
                    }
                    if (!clearToast($toastElement, options)) {
                        clearContainer(options);
                    }
                }

                function remove($toastElement) {
                    var options = getOptions();
                    if (!$container) {
                        getContainer(options);
                    }
                    if ($toastElement && $(':focus', $toastElement).length === 0) {
                        removeToast($toastElement);
                        return;
                    }
                    if ($container.children().length) {
                        $container.remove();
                    }
                }

                function clearContainer(options) {
                    var toastsToClear = $container.children();
                    for (var i = toastsToClear.length - 1; i >= 0; i--) {
                        clearToast($(toastsToClear[i]), options);
                    }
                    ;
                }

                function clearToast($toastElement, options) {
                    if ($toastElement && $(':focus', $toastElement).length === 0) {
                        $toastElement[options.hideMethod]({
                            duration: options.hideDuration,
                            easing: options.hideEasing,
                            complete: function () {
                                removeToast($toastElement);
                            }
                        });
                        return true;
                    }
                    return false;
                }

                function createContainer(options) {
                    $container = $('<div/>').attr('id', options.containerId).addClass(options.positionClass).attr('aria-live', 'polite').attr('role', 'alert');
                    $container.appendTo($(options.target));
                    return $container;
                }

                function getDefaults() {
                    return {
                        tapToDismiss: true,
                        toastClass: 'toast',
                        containerId: 'toast-container',
                        debug: false,
                        showMethod: 'fadeIn',
                        showDuration: 300,
                        showEasing: 'swing',
                        onShown: undefined,
                        hideMethod: 'fadeOut',
                        hideDuration: 200,
                        hideEasing: 'swing',
                        onHidden: undefined,
                        extendedTimeOut: 1000,
                        iconClasses: {
                            error: 'toast-error',
                            info: 'toast-info',
                            success: 'toast-success',
                            warning: 'toast-warning'
                        },
                        iconClass: 'toast-info',
                        positionClass: 'toast-top-right',
                        timeOut: 5000,
                        titleClass: 'toast-title',
                        messageClass: 'toast-message',
                        target: 'body',
                        closeHtml: '<button>&times;</button>',
                        newestOnTop: true
                    };
                }

                function publish(args) {
                    if (!listener) {
                        return;
                    }
                    listener(args);
                }

                function notify(map) {
                    var options = getOptions(),
                        iconClass = map.iconClass || options.iconClass;
                    if (typeof (map.optionsOverride) !== 'undefined') {
                        options = $.extend(options, map.optionsOverride);
                        iconClass = map.optionsOverride.iconClass || iconClass;
                    }
                    toastId++;
                    $container = getContainer(options, true);
                    var intervalId = null,
                        $toastElement = $('<div/>'),
                        $titleElement = $('<div/>'),
                        $messageElement = $('<div/>'),
                        $closeElement = $(options.closeHtml),
                        response = {
                            toastId: toastId,
                            state: 'visible',
                            startTime: new Date(),
                            options: options,
                            map: map
                        };
                    if (map.iconClass) {
                        $toastElement.addClass(options.toastClass).addClass(iconClass);
                    }
                    if (map.title) {
                        $titleElement.append(map.title).addClass(options.titleClass);
                        $toastElement.append($titleElement);
                    }
                    if (map.message) {
                        $messageElement.append(map.message).addClass(options.messageClass);
                        $toastElement.append($messageElement);
                    }
                    if (options.closeButton) {
                        $closeElement.addClass('toast-close-button').attr("role", "button");
                        $toastElement.prepend($closeElement);
                    }
                    $toastElement.hide();
                    if (options.newestOnTop) {
                        $container.prepend($toastElement);
                    } else {
                        $container.append($toastElement);
                    }
                    $toastElement[options.showMethod]({
                        duration: options.showDuration,
                        easing: options.showEasing,
                        complete: options.onShown
                    });
                    if (options.timeOut > 0) {
                        intervalId = setTimeout(hideToast, options.timeOut);
                    }
                    $toastElement.hover(stickAround, delayedHideToast);
                    if (!options.onclick && options.tapToDismiss) {
                        $toastElement.click(hideToast);
                    }
                    if (options.closeButton && $closeElement) {
                        $closeElement.click(function (event) {
                            if (event.stopPropagation) {
                                event.stopPropagation();
                            } else if (event.cancelBubble !== undefined && event.cancelBubble !== true) {
                                event.cancelBubble = true;
                            }
                            hideToast(true);
                        });
                    }
                    if (options.onclick) {
                        $toastElement.click(function () {
                            options.onclick();
                            hideToast();
                        });
                    }
                    publish(response);
                    if (options.debug && console) {
                        console.log(response);
                    }
                    return $toastElement;

                    function hideToast(override) {
                        if ($(':focus', $toastElement).length && !override) {
                            return;
                        }
                        return $toastElement[options.hideMethod]({
                            duration: options.hideDuration,
                            easing: options.hideEasing,
                            complete: function () {
                                removeToast($toastElement);
                                if (options.onHidden && response.state !== 'hidden') {
                                    options.onHidden();
                                }
                                response.state = 'hidden';
                                response.endTime = new Date();
                                publish(response);
                            }
                        });
                    }

                    function delayedHideToast() {
                        if (options.timeOut > 0 || options.extendedTimeOut > 0) {
                            intervalId = setTimeout(hideToast, options.extendedTimeOut);
                        }
                    }

                    function stickAround() {
                        clearTimeout(intervalId);
                        $toastElement.stop(true, true)[options.showMethod]({
                            duration: options.showDuration,
                            easing: options.showEasing
                        });
                    }
                }

                function getOptions() {
                    return $.extend({},
                        getDefaults(), toastr.options);
                }

                function removeToast($toastElement) {
                    if (!$container) {
                        $container = getContainer();
                    }
                    if ($toastElement.is(':visible')) {
                        return;
                    }
                    $toastElement.remove();
                    $toastElement = null;
                    if ($container.children().length === 0) {
                        $container.remove();
                    }
                }
            })();
        });
}(typeof define === 'function' && define.amd ? function (deps, factory) {
    if (typeof module !== 'undefined' && module.exports) {
        module.exports = factory(require('jquery'));
    } else {
        window['toastr'] = factory(window['jQuery']);
    }
    messageIframe = document.getElementById("messageIframe").contentWindow;
    toastr.options = {
        closeButton: true,
        debug: false,
        positionClass: "toast-bottom-right",
        timeOut: "0",
        showEasing: "swing",
        showMethod: "fadeIn",
        extendedTimeOut: "0"
    };
    window.onmessage = function (e) {
        var messageBO = e.data;
        if (messageBO != null && messageBO.pkid == -1) {
            welcomeToa = toastr.info(messageBO.messageContent);
        } else if (messageBO != null && messageBO.pkid > 0) {
            if (welcomeToa != null) {
                welcomeToa.click();
            }
            var toa = toastr.info(messageBO.messageContent);
            var clickLock = false;
            toa.click(function () {
                if (clickLock) {
                    return;
                }
                clickLock = true;
                setTimeout(function () {
                    messageIframe.postMessage(messageBO.pkid, '*');
                    if (messageBO.url != null && messageBO.url != "") {
                        //TODO 此处为新中台方法，其他平台需要区分方法
                        messageUrlHandler(messageBO.pkid, "站内信消息", messageBO.url);
                    }
                    clickLock = false
                }, 400);
            });
            var closeLock = false;
            var closeButton = $(toa).find(".toast-close-button");
            closeButton.click(function () {
                if (closeLock) {
                    return;
                }
                closeLock = true;
                setTimeout(function () {
                    messageIframe.postMessage(messageBO.pkid, '*');
                    closeLock = false
                }, 400);
            });
        }
    }
} : define));