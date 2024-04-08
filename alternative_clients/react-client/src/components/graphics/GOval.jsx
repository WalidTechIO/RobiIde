export default function GOval({oval, images}) {

    if (oval.dimension.width < 0) {
        oval.dimension.width = 24
    }
    if (oval.dimension.height < 0) {
        oval.dimension.height = 24
    }

    const style = {
        position: "absolute",
        width: `${oval.dimension.width}px`,
        height: `${oval.dimension.height}px`,
        backgroundColor: oval.color,
        top: oval.position.height,
        left: oval.position.width,
        borderRadius: "50%"
    }

    return <div style={style}>
        {oval.childrens.map(children => {
            return (<ObjectComputer key={children} object={children} images={images} />)
        })}
    </div>
}