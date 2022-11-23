const { createApp } = Vue;

createApp({
    data() {
        return {
            cliente: {},
            cuentas: [],
            id_prestamo: 0,
            id_aux: 0,
            monto: null,
            cuotas: null,
            numeroDeCuenta: "",
            prestamos: [],
            prestamo_elegido: {},
            monto_maximo: null,
            montoFinal: {},

        }
    },

    created() {
        this.getData("/api/clients/current")
    },

    methods: {
        getData(url) {
            axios.get(url).then(response => {
                this.cliente = response.data
                this.cuentas = this.cliente.accounts
                this.traer_prestamos()
            })

        },
        tirarError(titulo, icono, position) {
            const Toast = Swal.mixin({
                toast: true,
                position: position,
                showConfirmButton: false,
                timer: 3500,
                timerProgressBar: true,
                background: "#212529",
                color: "#fee6ba",
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })

            Toast.fire({
                icon: icono,
                title: titulo
            })
        },
        cerrarSesion() {
            Swal.fire({
                title: 'Estás seguro de cerrar la sesión?',
                // text: "que quieres salir?",
                icon: 'warning',
                showCancelButton: true,
                background: "#212529",
                color: "#fee6ba",
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Si!',
                cancelButtonText: 'No!'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout').then(response => {
                        localStorage.clear()
                        window.location.pathname = "/index.html"
                    })
                }
            })
        },
        traer_monto_maximo() {
            if (this.id_prestamo != this.id_aux && this.id_prestamo != 0) {
                this.id_aux = this.id_prestamo
                setTimeout(() => {
                    this.monto_maximo = this.prestamo_elegido.maxAmount
                    console.log(1);
                }, 300)

            }
        },
        // traer_monto_maximo() {
        //     if (this.id_prestamo != 0) {
        //         this.monto_maximo = this.prestamo_elegido.maxAmount
        //         console.log(this.monto_maximo);
        //         return new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(this.monto_maximo)
        //     }
        //     return "$0"
        // },
        traer_prestamos() {
            axios.get("/api/loans").then(response => {
                this.prestamos = response.data
            })
        },
        hacer_porcentaje() {
            console.log("a");
            if (this.prestamo_elegido != null) {
                console.log("b");
                setTimeout(() => {

                    this.montoFinal = {
                        montoFinal: this.monto * this.prestamo_elegido.percentage,
                        porcentaje: (this.prestamo_elegido.percentage + "").slice(2, 4)
                    }
                }, 300)
            }
        },
        traer_prestamo_elegido() {
            let prestamo = this.prestamos.find(prestamo => prestamo.id == this.id_prestamo)
            this.prestamo_elegido = prestamo
            // console.log("a");
        },
        solicitar_prestamo() {
            Swal.fire({
                title: 'Quieres hacer la transacción con los datos seleccionados?',
                text: `Préstamo:${this.prestamo_elegido.name} - Pagos:${this.cuotas} - Monto:$${this.monto} - Cuenta:${this.numeroDeCuenta}`,
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
                    axios.post('/api/loans', { idLoan: this.id_prestamo, amount: this.monto, payments: this.cuotas, numberAccount: this.numeroDeCuenta })
                        .then(() => this.tirarError("Se completó la solicitud con ÉXITO", "success", "top-end"))
                        .catch(error => {
                            let status = error.request.response;
                            if (status == "You aren't an authenticated customer") {
                                this.tirarError("No tienes permiso para hacer esta peticion", "error", "center")
                            }
                            if (status == "Account number is empty or invalid") {
                                this.tirarError("El número de la cuenta no es valido 0 esta vacío", "error", "center")
                            }
                            if (status == "Payments number is empty or invalid") {
                                this.tirarError("La cuota elegida no existe(es igual o menor a 0)", "error", "center")
                            }
                            if (status == "Id loan number is empty or invalid") {
                                this.tirarError("El ID del préstamo esta vacío o es invalido", "error", "center")
                            }
                            if (status == "Amount number is empty or invalid") {
                                this.tirarError("El numero del MONTO esta vacío o es invalido", "error", "center")
                            }
                            if (status == "The requested loan does not exist") {
                                this.tirarError("El préstamo solicito no existe", "error", "center")
                            }
                            if (status == "The requested amount is greater than the maximum amount") {
                                this.tirarError("El monto solicitado es mayor a el monto maximo", "error", "center")
                            }
                            if (status == "The selected account does not exist") {
                                this.tirarError("La cuenta seleccionada no exite", "error", "center")
                            }
                            if (status == "The account you select is not yours") {
                                this.tirarError("La cuenta seleccionada no es tuya", "error", "center")
                            }
                            if (status == "You can't have two loans of the same type at the same time") {
                                this.tirarError("No podes tener dos prestamos del mismo tipo a la vez", "error", "center")
                            }
                            if (error.message == "Request failed with status code 400") {
                                this.tirarError("Ocurrió un error, debes seguir el paso a paso", "error", "top-end")
                            }
                        })
                }
            })
        }
    },

    computed: {


    },

}).mount("#app");