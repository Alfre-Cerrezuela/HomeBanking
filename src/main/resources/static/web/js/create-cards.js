const { createApp } = Vue;

createApp({
    data() {
        return {
            cliente: {},
            tarjetaTipo: "",
            tarjetaColor: "",
            tarjetaMarca: "",
            nombreYapellido: "",
            event: new Date(Date.UTC(2012, 11, 20, 3, 0, 0)),
            options: { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' },
        }
    },

    created() {
        this.getData("/api/clients/current")
    },

    methods: {
        getData(url) {
            axios.get(url).then(response => {
                this.cliente = response.data
                this.tarjetas = this.cliente.cards
                console.log(this.cliente);
                this.nombreYapellido = this.cliente.firstName + " " + this.cliente.lastName
            })

        },
        tirarError(titulo, icono, position) {
            const Toast = Swal.mixin({
                toast: true,
                position: position,
                showConfirmButton: false,
                timer: 3500,
                timerProgressBar: true,
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
        girarTarjeta() {
            let tarjetaAdelante = document.getElementById("tarjetaAdelante")
            let tarjetaAtras = document.getElementById("tarjetaAtras")
            tarjetaAdelante.classList.toggle("voltear")
            tarjetaAtras.classList.toggle("girar")
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
                        localStorage.clear()
                        window.location.pathname = "/index.html"
                    })
                }
            })
        },
        crearTarjeta() {
            Swal.fire({
                title: 'Quieres crear la tarjeta que seleccionas tarjeta',
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

                    axios.post('/api/clients/current/cards', `cardColor=${this.tarjetaColor}&cardType=${this.tarjetaTipo}&visa_mastercard=${this.tarjetaMarca}`)
                        .then(() => this.tirarError("Se creó tu tarjeta", "success", "top-end"))
                        .catch(error => {

                            let status = error.request.response;

                            if (status == "You can't have more than 3 cards of the same type") {
                                this.tirarError("No puedes tener más de 3 tarjetas del mismo tipo", "error", "center")
                            }

                            if (status == "card color is invalid (is null)") {
                                this.tirarError("Selecciona el COLOR de la tarjeta", "error", "center")
                            }

                            if (status == "card type is invalid (is null)") {
                                this.tirarError("Selecciona el TIPO de la tarjeta", "error", "center")
                            }

                            if (status == "card visa or mastercard is invalid (is null)") {
                                this.tirarError("Selecciona si quieres VISA o MASTERCARD", "error", "center")
                            }

                            if (status == "card color is invalid (only SILVER, TITANIUM or GOLD)") {
                                this.tirarError("El COLOR de la tarjeta solo puede ser SINVER, TITANIUM o GOLD", "error", "center")
                            }

                            if (status == "card type is invalid (only DEBIT or DEBIT)") {
                                this.tirarError("El TIPO de la tarjeta SOLO puede ser DEBIT o CREDIT", "error", "center")
                            }

                            if (status == "card Visa or card MasterCard is invalid (only VISA or MASTERCARD)") {
                                this.tirarError("SOLO puede ser VISA o MASTERCARD", "error", "center")
                            }
                            if (status == "You can't have more than 1 cards of the same color") {
                                this.tirarError("No podes tener mas de una tarjeta del mismo tipo y color", "error", "center")
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