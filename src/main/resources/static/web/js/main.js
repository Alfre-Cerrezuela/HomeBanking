const { createApp } = Vue;

createApp({
    data() {
        return {
            // arrayClientes: [],
            /*  cuentas: [] */
            cliente: {},
            tarjetasDebito: [],
            tarjetasCredito: [],
            id_borrar_cuenta: "",
            contraseñaAfirmar: "",
            tipoCuenta: "",

        }
    },
    mounted() { /* es cuando se creo la parte visual cuando este renderizado */

    },

    created() {
        this.getData("/api/clients/current")
    },

    methods: {
        getData(url) {
            axios.get(url).then(response => {
                this.cliente = response.data
                /*  this.cuentas = this.arrayClientes.map(cliente => cliente.account) */
                // let local = JSON.parse(localStorage.getItem("cliente"))
                // if (local) {
                //     this.cliente = local
                // }
                console.log(this.cliente);

                this.sacarCuentas()
            })
        },
        sumaTotal(cliente) {
            let total = 0
            cliente.accounts.forEach(cuenta => total = total + cuenta.balance)
            return total
        },
        cerrarSesion() {
            Swal.fire({
                title: 'Estás seguro de cerrar la sesión?',
                // text: "que quieres salir?",
                icon: 'warning',
                showCancelButton: true,
                background: "#212529",
                color: "#ffffff",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si!',
                cancelButtonText: 'No!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout').then(response => {
                        localStorage.setItem('estaLogueado', 'false')
                        window.location.pathname = "/index.html"
                    })
                }
            })
        },
        sacarCuentas() {
            this.cliente.cards.forEach(tarjeta => {

                if (tarjeta.type == "DEBIT") {
                    this.tarjetasDebito.push(tarjeta)
                } else {
                    this.tarjetasCredito.push(tarjeta)
                }
            })
        },
        tirarError(titulo, logo) {
            const Toast = Swal.mixin({
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 3500,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })

            Toast.fire({
                icon: logo,
                title: titulo
            })
        },
        crearCuentas() {
            console.log(this.tipoCuenta);
            Swal.fire({
                title: 'Estás seguro de crear una nueva cuenta?',
                // text: "que quieres salir?",
                icon: 'question',
                showCancelButton: true,
                background: "#212529",
                color: "#ffffff",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si!',
                cancelButtonText: 'No!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/clients/current/accounts", `password=${this.contraseñaAfirmar}&typeAccount=${this.tipoCuenta}`)
                        .then(() => {
                            this.tipoCuenta = ""
                            this.contraseñaAfirmar = ""
                            this.getData("/api/clients/current")
                        }).catch(error => {
                            // console.log(error);
                            let status = error.request.response

                            if (status == "You can not have more than 3 accounts", "error") {
                                this.tirarError("No puedes tener mas de 3 cuentas", "error")
                            } else {
                                this.tirarError("Ocurrió un error")
                            }
                        })
                }
            })
        },
        ir_a_solicitar_prestamo() {
            window.location.href = "/web/loan-application.html"
        },
        borrar_cuenta() {
            Swal.fire({
                title: 'Estás seguro de borrar la cuenta seleccionada?',
                text: "Tambien se borrán sus transacciones",
                icon: 'warning',
                showCancelButton: true,
                background: "#212529",
                color: "#ffffff",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si!',
                cancelButtonText: 'No!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch('/api/clients/current/accounts', `id=${this.id_borrar_cuenta}&password=${this.contraseñaAfirmar}`).then(() => {
                        this.contraseñaAfirmar = ""
                        this.tirarError("La cuenta se borró con éxito", "success")
                        this.getData("/api/clients/current")
                    })
                }
            })
        }




    },

    computed: {

    },

}).mount("#app");