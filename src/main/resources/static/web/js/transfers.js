const { createApp } = Vue;

createApp({
    data() {
        return {
            cliente: {},
            cuentas: [],
            propia_tercero: "",
            cuenta_origen_num: "",
            cuenta_destino_num: "",
            monto: null,
            cuenta_balance: 0,
            descripción: "",


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
                console.log(this.cliente);
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
        traer_balance() {
            if (this.cuenta_origen_num != "") {
                let cuenta_elegida = this.cuentas.find(cuenta => cuenta.number == this.cuenta_origen_num)
                let balance = cuenta_elegida.balance
                this.cuenta_balance = parseFloat(balance)
                return new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' }).format(balance)

            }
            else {
                return "$0"
            }
        },
        hacer_transaccion() {
            if (this.propia_tercero != "propia" && this.propia_tercero != "tercero") {
                this.tirarError("Debes seleccionar un de donde va a ser la cuenta de destino", "error", "center")
                return 0
            } else if (this.cuenta_origen_num == "") {
                this.tirarError("Debes seleccionar una cuenta de ORIGEN", "error", "center")
                return 0
            } else if (this.cuenta_destino_num == "") {
                this.tirarError("Debes seleccionar una cuenta de DESTINO", "error", "center")
                return 0
            } else if (this.monto <= 0) {
                this.tirarError("El MONTO no puede ser de 0 o menor a él", "error", "center")
                return 0
            } else if (this.descripción == "") {
                this.tirarError("Debes colocar la DESCRIPCIÓN", "error", "center")
                return 0
            } else {
                Swal.fire({
                    title: 'Quieres hacer la transacción con los datos seleccionados?',
                    text: `De:${this.cuenta_origen_num} - Para:${this.cuenta_destino_num} - Monto:$${this.monto}`,
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
                        axios.post('/api/transactions', `amount=${this.monto}&description=${this.descripción}&numberSourceAccount=${this.cuenta_origen_num.toUpperCase()}&numberDestinationAccount=${this.cuenta_destino_num.toUpperCase()}`)
                            .then(() => this.tirarError("Se completó la transacción con ÉXITO", "success", "top-end"))
                            .catch(error => {
                                let status = error.request.response;
                                if (status == "Amount isn't valid or is equal to $0.0") {
                                    this.tirarError("El MONTO no es valido, o es igual a $0, o es menor a $0", "error", "center")
                                }
                                if (status == "Description isn't valid or is empty.") {
                                    this.tirarError("La DESCRIPCIÓN esta vacía", "error", "center")
                                }
                                if (status == "The number of the source isn't valid or is empty.") {
                                    this.tirarError("El numero de la cuenta de ORIGEN no es valido o esta vacío (ponlo en mayúsculas)", "error", "center")
                                }
                                if (status == "The number of the destination isn't valid or is empty") {
                                    this.tirarError("El numero de la cuenta de DESTINO no es valido o esta vacío", "error", "center")
                                }
                                if (status == "The number of the destination account doesn't have to be equal to source account number") {
                                    this.tirarError("El de la cuenta de ORIGEN no debe ser igual al de la de DESTINO", "error", "center")
                                }
                                if (status == "The source account not exist.") {
                                    this.tirarError("La cuenta de DESTINO no existe", "error", "center")
                                }
                                if (status == "The source account isn't one of your accounts.") {
                                    this.tirarError("La cuenta de ORIGEN no es una de tus cuentas", "error", "center")
                                }
                                if (status == "The destination account not exist.") {
                                    this.tirarError("La cuenta de DESTINO no existe", "error", "center")
                                }
                                if (status == "The selected account does not have the available balance.") {
                                    this.tirarError("La cuenta seleccionada no tiene ese SALDO disponble", "error", "center")
                                }
                                if (error.message == "Request failed with status code 400") {
                                    this.tirarError("Ocurrió un error, debes seguir el paso a paso", "error", "top-end")
                                }
                            })
                    }
                })
            }
        }





    },

    computed: {

    },

}).mount("#app");