const { createApp } = Vue;

createApp({
    data() {
        return {
            email: "",
            contraseña: "",
            nombre: "",
            apellido: "",
            emailNuevo: "",
            contraseñaNueva: "",


        }
    },

    created() {
    },

    methods: {
        login() {
            axios.post('/api/login', `email=${this.email}&password=${this.contraseña}`).then(() => {
                localStorage.setItem('estaLogueado', 'true')
                if (this.email.startsWith("admin")) {
                    window.location.pathname = "/admin/manager.html"
                } else {
                    window.location.pathname = "/web/accounts.html"
                }
            }).catch(error => {
                console.log(error);
                let status = error.request.status
                if (status == 401) {
                    this.tirarError("El email o la contraseña no existen")
                } else {
                    this.tirarError("Ocurrió un error!")
                }
            })

        },
        girarTarjeta() {
            let tarjetaAdelante = document.getElementById("tarjetaAdelante")
            let tarjetaAtras = document.getElementById("tarjetaAtras")
            tarjetaAdelante.classList.toggle("voltear")
            tarjetaAtras.classList.toggle("girar")
        },
        tirarError(titulo) {
            const Toast = Swal.mixin({
                toast: true,
                position: 'center',
                showConfirmButton: false,
                timer: 3500,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })

            Toast.fire({
                icon: 'error',
                title: titulo
            })
        },
        registrarCliente() {
            this.email = this.emailNuevo
            this.contraseña = this.contraseñaNueva
            axios.post('/api/clients', `firstName=${this.nombre}&lastName=${this.apellido}&email=${this.emailNuevo}&password=${this.contraseñaNueva}`)
                .then(() => this.login())
                .catch(error => {
                    let status = error.response.data
                    console.log(status);
                    if (status == "Missing firstName") {
                        this.tirarError("El campo 'NOMBRE' está incompleto")
                    }
                    if (status == "Missing lastName") {
                        this.tirarError("El campo 'APELLIDO' está incompleto")
                    }
                    if (status == "Missing email") {
                        this.tirarError("El campo 'EMAIL' está incompleto")
                    }
                    if (status == "Missing password") {
                        this.tirarError("El campo 'CONTRASEÑA' está incompleto")
                    }

                })
        },
        mostrarContraseña(idContra, idSpan) {
            let pswrd = document.getElementById(idContra);
            let toggleBtn = document.getElementById(idSpan);
            toggleBtn.onclick = function () {
                if (pswrd.type === 'password') {
                    pswrd.setAttribute('type', 'text');
                    toggleBtn.classList.add('hidden');

                } else {
                    pswrd.setAttribute('type', 'password');
                    toggleBtn.classList.remove('hidden');
                }
            }
        }


    },

    computed: {

    },

}).mount("#app");