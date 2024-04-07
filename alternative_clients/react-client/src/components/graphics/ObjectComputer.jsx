import Rect from "./Rect.jsx";

export default function ObjectComputer({object, images}) {

    //World rendering
    if(object.type === "WORLD") {

        if (object.dimension.width < 0) {
            object.dimension.width = 200
        }
        if (object.dimension.height < 0) {
            object.dimension.height = 200
        }

        const style = {
            position: "relative",
            backgroundColor: object.color,
            width: `${object.dimension.width}px`,
            height: `${object.dimension.height}px`,
        }

        return <div style={style}>
            {object.childrens.map(children => {
                return (<ObjectComputer key={children} object={children} />)
            })}
        </div>
    }

    //Common objects
    return <>
        {object.type === "RECT" && <Rect rectangle={object} />}
        {object.type === "IMAGE" && <Image image={object} images={images} />}
    </>

}