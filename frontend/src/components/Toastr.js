import toastr from 'toastr';

toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": false,
    "progressBar": true,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "400",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
  }

function showMessage(title, message, type) {
    toastr[type](message, title);
}

function errorMessage(message) {
    showMessage('Erro', message, 'error');
}

function successMessage(message) {
    showMessage('Sucesso', message, 'success');
}

function warningMesssage(message) {
    showMessage('Alerta', message, 'warning');
}

export { 
    showMessage,
    errorMessage,
    successMessage,
    warningMesssage
}