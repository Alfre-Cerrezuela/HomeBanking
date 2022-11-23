const { createApp } = Vue;

createApp({
    data() {
        return {
            queHacer: null,

            nombrePrestamo: "",
            montoMaximoPrestamo: "",
            pagoPrestamo: null,
            porcentajePrestamo: null,
            pagosPrestamo: [],

            prestamosCreados: [],

            okey: false,
            idPrestamoElegido: null,
            prestamoElegido: {},
            nombrePrestamoModificar: "",
            montoMaximoPrestamoModificar: "",
            pagoPrestamoModificar: null,
            porcentajePrestamoModificar: null,
            pagosPrestamoModificar: [],

            idPrestamoBorrar: null
        }
    },


    created() {
        this.traerPrestamos()
    },

    methods: {
        traerPrestamos() {
            axios.get("/api/loans")
                .then(response => {
                    this.prestamosCreados = response.data
                    console.log(this.prestamosCreados);
                })
        },
        completarInputPrestamoElegido() {
            this.okey = true
            this.prestamoElegido = this.prestamosCreados.find(prestamo => prestamo.id == this.idPrestamoElegido)
            this.nombrePrestamoModificar = this.prestamoElegido.name;
            this.montoMaximoPrestamoModificar = this.prestamoElegido.maxAmount;
            this.pagosPrestamoModificar = this.prestamoElegido.payments
            this.porcentajePrestamoModificar = this.prestamoElegido.percentage
        },
        agregarCuota() {
            if (this.pagoPrestamo != null) {
                this.pagosPrestamo.push(this.pagoPrestamo)
                this.pagoPrestamo = null
            }
        },
        borrarCuota() {
            if (this.pagosPrestamo.length != 0) {
                this.pagosPrestamo.pop()
            }
        },
        agregarCuotaModificar() {
            if (this.pagoPrestamoModificar != null) {
                this.pagosPrestamoModificar.push(this.pagoPrestamoModificar)
                this.pagoPrestamoModificar = null
            }
        },
        borrarCuotaModificar() {
            if (this.pagosPrestamoModificar.length != 0) {
                this.pagosPrestamoModificar.pop()
            }
        },
        crearPrestamo() {
            axios.post("/api/loans/create", { name: this.nombrePrestamo, maxAmount: this.montoMaximoPrestamo, payments: this.pagosPrestamo, percentage: this.porcentajePrestamo })
                .then(() => {
                    this.nombrePrestamo = ""
                    this.montoMaximoPrestamo = null
                    this.pagosPrestamo = []
                    this.porcentajePrestamo = null
                    console.log("se creo el prestamo")
                    this.traerPrestamos()
                })
                .catch(error => console.log(error))
        },
        modificarPrestamo() {
            axios.put("/api/loans/put", `id=${this.idPrestamoElegido}&nameLoan=${this.nombrePrestamoModificar}&maxAmountLoan=${this.montoMaximoPrestamoModificar}&paymentsLoan=${this.pagosPrestamoModificar}&percentageLoan${this.porcentajePrestamoModificar}`)
                .then(() => {
                    this.idPrestamoElegido = null
                    this.nombrePrestamoModificar = ""
                    this.montoMaximoPrestamoModificar = null
                    this.pagosPrestamoModificar = []
                    this.porcentajePrestamoModificar = null
                    console.log("se creo el prestamo")
                    this.traerPrestamos()
                })
                .catch(error => console.log(error))
        },
        borrarPrestamo() {
            axios.delete("/api/loans/delete", "id=" + this.idPrestamoBorrar)
                .then(() => {
                    this.traerPrestamos()
                    console.log("se borro exitosamente")
                })
                .catch(error => console.log(error))
        }


    },

    computed: {

    },

}).mount("#app");
